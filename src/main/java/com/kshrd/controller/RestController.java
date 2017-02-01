package com.kshrd.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.SelfDescribing;
import org.neo4j.cypher.internal.compiler.v2_1.perty.printToString;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.PayloadBuilder;

@Controller
public class RestController {

	// Set Value from Controller to View
	@RequestMapping(value = "/")
	public String showHomePage(ModelMap model) {
		return "index_angularjs";
	}

	public static void main(String[] arg) {
		RestController obj = new RestController();
		obj.applePush() ;
	}

	private void firebasePush() {
		/* FIREBASE */
		// reference :
		// https://firebase.google.com/docs/cloud-messaging/http-server-ref#notification-payload-support
		// https://github.com/AndreiD/Spring-Android-Push-Notifications-FCM-

		// Message body
		Map<String, Object> message = new HashMap<>();
		message.put("priority", "high");

		// Single token
		message.put("to",
				"dBbB2BFT-VY:APA91bHrvgfXbZa-K5eg9vVdUkIsHbMxxxxxc8dBAvoH_3ZtaahVVeMXP7Bm0iera5s37ChHmAVh29P8aAVa8HF0I0goZKPYdGT6lNl4MXN0na7xbmvF25c4ZLl0JkCDm_saXb51Vrte");

		// Multiple token
		String[] ids = {
				"dkPe1Q3IoNo:APA91bHIbrKk9NmCXDCp-HTl8PCXT7huE6Hav5YV6pw5Jr7d5FKRhSny8fuOgQ2POlza6LVvAlbMqURMuqgpWwFIgufj_Cgd_XbimC_iZQkPNjzgZk0f7UpNZGkGXzlORqg4PeHtDG7r",
				"dkPe1Q3IoNo:APA91bHIbrKk9NmCXDCp-HTl8PCXT7huE6Hav5YV6pw5Jr7d5FKRhSny8fuOgQ2POlza6LVvAlbMqURMuqgpWwFIgufj_Cgd_XbimC_iZQkPNjzgZk0f7UpNZGkGXzlORqg4PeHtDG3r" };
		message.put("registration_ids", ids);

		// Notification property : check it Firebase website for more detail
		Map<String, Object> notification = new HashMap<>();
		notification.put("title", "This is title");
		notification.put("text", "This is text");
		notification.put("body", "This body");
		notification.put("sound", "cat.caf");
		notification.put("badge", "5");
		// Add Notification property to notification key
		message.put("notification", notification);

		// Custom data : check it Firebase website for more detail
		Map<String, Object> data = new HashMap<>();
		data.put("key1", "value1");
		data.put("key2", "value2");
		// Add data property to data key
		message.put("data", data);

		// Header
		HttpHeaders headers = new HttpHeaders();
		// Firebase App ID
		headers.add("project_id", "1:216399706344:ios:3ecdf5e0f46c2920");

		// Firebase Cloud Messaging ==>> Legacy Server key
		headers.add("Authorization", "key=AIzaSyCJQeM-XoFtSutpA8rABJQS3bAJ6IiscQA");

		// Add content-type and accept : application/json
		headers.add("Accept", "application/json");
		headers.add("Content-Type", "application/json");

		// HttpEntity : Add Firebase message property and Header to HttpEntity
		HttpEntity<Map> entity = new HttpEntity<Map>(message, headers);

		// RestTemplate
		RestTemplate rest = new RestTemplate();
		String firebaseApiUrl = "https://fcm.googleapis.com/fcm/send";
		ResponseEntity<Map> response = rest.exchange(firebaseApiUrl, HttpMethod.POST, entity, Map.class);

		// Response from Firebase API
		System.out.println(response.getBody());
	}

	private void applePush() {
		System.out.println("Sending an iOS push notification...");

		String token = "DF3AD4728D8A1BC47BB81DEC9447821EDE1F0B3BF19113D10F615515930643B0";
		String type = "dev";
		String message = "the test push notification message";

		System.out.println("The target token is " + token);

		// Create Apns Service builder
		ApnsServiceBuilder serviceBuilder = APNS.newService();

		// Check production or development
		if (type.equals("prod")) {
			System.out.println("using prod API");
			String certPath = RestController.class.getResource("prod_cert.p12").getPath();
			serviceBuilder.withCert(certPath, "password").withProductionDestination();
		} else if (type.equals("dev")) {
			System.out.println("using dev API");
			//String certPath = RestController.class.getResource("aps_dev_credentials.p12").getPath();
			
			File file = new File("dev_cert.p12");
			serviceBuilder.withCert(file.getAbsolutePath(), "password").withSandboxDestination();
		} else {
			System.out.println("unknown API type " + type);
			return;
		}

		// Create Apns Service
		ApnsService service = serviceBuilder.build();
		// Payload with custom fields
		String payload = APNS.newPayload()
				.customField("title", "title")
				.alertBody(message).badge(10)
				.sound("default")
				.customField("custom", "custom value")
				.build();

		// /* APPLE */
		// // reference:

		// http://stackoverflow.com/questions/11236630/can-apple-push-notifications-send-more-parameters-than-alert-and-sound
		// /* The root Payload */
		// Map<String, Object> payload = new HashMap<>();
		// /* The application Dictionnary */
		// Map<String, Object> apsDictionary = new HashMap<>();
		// apsDictionary.put("badge", 1);
		// apsDictionary.put("sound", "cat.caf");
		// apsDictionary.put("alert", "alert");
		// payload.put("aps", apsDictionary);

		// // custom dictionnary with a string value
		// payload.put("customName", "customValue");
		// payload.put("message", "Some custom message for your app");
		// payload.put("id", 1234);

		//// Payload with custom fields
		// String payload = APNS.newPayload()
		// .alertBody(message).build();

		//// String payload example:
		// String payload = "{\"aps\":{\"alert\":{\"title\":\"My Title
		//// 1\",\"body\":\"My message 1\",\"category\":\"Personal\"}}}";

		System.out.println("payload: " + payload);
		service.push(token, payload);

		System.out.println("The message has been hopefully sent...");

	}
}
