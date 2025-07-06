package com.yujun.yuaiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBasedChatMemory implements ChatMemory {

    private final String Base_DIR;

    private static final Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);
        //
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    public FileBasedChatMemory(String dir) {
        this.Base_DIR = dir;
        File baseDir = new File(dir);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }


    @Override
    public void add(String conversationId, Message message) {
        add(conversationId, List.of(message));
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> existingMessages = getOrCreateConversation(conversationId);
        existingMessages.addAll(messages);
        saveConversation(conversationId, existingMessages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> existingMessages = getOrCreateConversation(conversationId);
        if (existingMessages.size() > lastN) {
            return existingMessages.subList(existingMessages.size() - lastN, existingMessages.size());
        } else {
            return existingMessages;
        }
    }

    @Override
    public void clear(String conversationId) {
        File conversationFile = getConversationFile(conversationId);
        if (conversationFile.exists()) {
            conversationFile.delete();
        } else {
            throw new IllegalArgumentException("Conversation with ID " + conversationId + " does not exist");
        }
    }

    /**
     * Retrieves the conversation messages associated with the given conversation ID.
     * If the conversation file exists, the messages are deserialized and returned.
     * If the conversation file does not exist, an empty list is returned, representing
     * the creation of a new conversation.
     *
     * @param conversationId the unique identifier of the conversation
     * @return a list of {@code Message} objects associated with the specified conversation ID
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        File conversationFile = getConversationFile(conversationId);
        List<Message> messages = new ArrayList<>();

        if (conversationFile.exists()){
            try (Input input = new Input(new FileInputStream(conversationFile))) {
                messages = kryo.readObject(input, ArrayList.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }

    /**
     * Persists a conversation to a file based on the given conversation ID and list of messages.
     * The messages are serialized and written to a file within the specified base directory.
     *
     * @param conversationId the unique identifier of the conversation to be saved
     * @param messages the list of {@code Message} objects to be stored for the specified conversation ID
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        File conversationFile = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(conversationFile))) {
            kryo.writeObject(output, messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Retrieves the file associated with a specific conversation based on its ID.
     * The file is created or resolved within the specified base directory, with
     * the provided conversation ID as its name and ".kryo" as an extension.
     *
     * @param conversationId the unique identifier of the conversation for which the file is retrieved
     * @return a {@code File} object representing the conversation's file
     */
    private File getConversationFile(String conversationId) {
        return new File(Base_DIR, conversationId + ".kryo");
    }
}
