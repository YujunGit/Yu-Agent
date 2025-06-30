package com.yujun.yuaiagent.advisor;

import org.springframework.ai.chat.client.advisor.api.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * Advisor that modifies user requests by repeating the input query in a template format.
 * Implements both synchronous and streaming interfaces to intercept and transform
 * AI conversation requests before processing.
 */
public class ReReadingAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

	/**
	 * Modifies the provided `AdvisedRequest` to include additional user parameters and
	 * reformatted user text for downstream processing.
	 *
	 * @param advisedRequest the original `AdvisedRequest` containing the user query and parameters.
	 * @return a new `AdvisedRequest` instance with updated user parameters and reformatted text.
	 */
	private AdvisedRequest before(AdvisedRequest advisedRequest) {

		Map<String, Object> advisedUserParams = new HashMap<>(advisedRequest.userParams());
		advisedUserParams.put("re2_input_query", advisedRequest.userText());

		return AdvisedRequest.from(advisedRequest)
			.userText("""
			    {re2_input_query}
			    Read the question again: {re2_input_query}
			    """)
			.userParams(advisedUserParams)
			.build();
	}

	@Override
	public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
		return chain.nextAroundCall(this.before(advisedRequest));
	}

	@Override
	public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
		return chain.nextAroundStream(this.before(advisedRequest));
	}

	@Override
	public int getOrder() { 
		return 0;
	}

    @Override
    public String getName() { 
		return this.getClass().getSimpleName();
	}
}