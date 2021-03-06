/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.sf.coworkercommunicator.lambda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.sf.coworkercommunicator.mail.EmailMessage;
import com.sf.coworkercommunicator.mail.Mailer;
import com.amazon.speech.ui.OutputSpeech;

public class CoworkerCommunicatorSpeechlet implements SpeechletV2 {
	private static final Logger log = LoggerFactory.getLogger(CoworkerCommunicatorSpeechlet.class);

	@Override
	public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
		log.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId());
		// any initialization logic goes here
	}

	@Override
	public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
		log.info("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId());
		return getWelcomeResponse();
	}

	@Override
	public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		IntentRequest request = requestEnvelope.getRequest();
		Intent intent = request.getIntent();
		
		String recipient = intent.getSlot("recipient").getValue();
		String sender = intent.getSlot("sender").getValue();
		String id = intent.getSlot("message").getResolutions().getResolutionsPerAuthority().get(0).getValueWrappers().get(0).getValue().getId();
		
		String intentName = (intent != null) ? intent.getName() : null;

		log.info("onIntent requestId={}, sessionId={}, intentName={}", request.getRequestId(), requestEnvelope.getSession().getSessionId(), intentName);
		
		if ("MessageIntent".equals(intentName)) {
			return sendEmail(recipient, sender, id);
		} else if ("AMAZON.HelpIntent".equals(intentName)) {
			return getHelpResponse();
		} else {
			return getAskResponse("HelloWorldTest", "This is unsupported.  Please try something else. intent name : " + intentName);
		}
	}
	
	private SpeechletResponse sendEmail(String recipient, String sender, String id) {
		EmailMessage result = new Mailer().sendMessage(recipient, sender, id);
		PlainTextOutputSpeech speech = (result == null) ? getPlainTextOutputSpeech("Error sending message to " + recipient) : getPlainTextOutputSpeech("A \"" + result.getSpokenText() + "\" email has been sent to " + recipient + " from " + sender);
		return SpeechletResponse.newTellResponse(speech);
	}

	@Override
	public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
		log.info("onSessionEnded requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId());
		// any cleanup logic goes here
	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with a welcome message.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getWelcomeResponse() {
		String speechText = "Welcome to the Alexa Skills Kit, you can say hello";
		return getAskResponse("HelloWorld", speechText);
	}

	/**
	 * Creates a {@code SpeechletResponse} for the help intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getHelpResponse() {
		String speechText = "You can say hello to me!";
		return getAskResponse("HelloWorld", speechText);
	}

	/**
	 * Helper method that creates a card object.
	 * 
	 * @param title
	 *            title of the card
	 * @param content
	 *            body of the card
	 * @return SimpleCard the display card to be sent along with the voice response.
	 */
	private SimpleCard getSimpleCard(String title, String content) {
		SimpleCard card = new SimpleCard();
		card.setTitle(title);
		card.setContent(content);

		return card;
	}

	/**
	 * Helper method for retrieving an OutputSpeech object when given a string of
	 * TTS.
	 * 
	 * @param speechText
	 *            the text that should be spoken out to the user.
	 * @return an instance of SpeechOutput.
	 */
	private PlainTextOutputSpeech getPlainTextOutputSpeech(String speechText) {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		return speech;
	}

	/**
	 * Helper method that returns a reprompt object. This is used in Ask responses
	 * where you want the user to be able to respond to your speech.
	 * 
	 * @param outputSpeech
	 *            The OutputSpeech object that will be said once and repeated if
	 *            necessary.
	 * @return Reprompt instance.
	 */
	private Reprompt getReprompt(OutputSpeech outputSpeech) {
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(outputSpeech);

		return reprompt;
	}

	/**
	 * Helper method for retrieving an Ask response with a simple card and reprompt
	 * included.
	 * 
	 * @param cardTitle
	 *            Title of the card that you want displayed.
	 * @param speechText
	 *            speech text that will be spoken to the user.
	 * @return the resulting card and speech text.
	 */
	private SpeechletResponse getAskResponse(String cardTitle, String speechText) {
		SimpleCard card = getSimpleCard(cardTitle, speechText);
		PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
		Reprompt reprompt = getReprompt(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}
}
