package com.bajaj.webhook;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class WebhookApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(WebhookApplication.class, args);
	}

@Override
	public void run(String... args) throws Exception {

		
		RestTemplate restTemplate = new RestTemplate();

		
		String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "Chandana H H");
		requestBody.put("regNo", "REG12345");
		requestBody.put("email", "chandanahh684@gmail.com");


		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

		
		ResponseEntity<Map> response =
				restTemplate.postForEntity(generateUrl, entity, Map.class);

		System.out.println("Generate Webhook Response: " + response.getBody());

		String webhookUrl = (String) response.getBody().get("webhook");
		String accessToken = ((String) response.getBody().get("accessToken")).trim();

		System.out.println("Webhook URL: " + webhookUrl);
		System.out.println("Access Token: " + accessToken);

		

String finalQuery =
        "SELECT d.DEPARTMENT_NAME, t.SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS EMPLOYEE_NAME, " +
        "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE " +
        "FROM ( SELECT emp_id, SUM(amount) AS SALARY FROM PAYMENTS " +
        "WHERE DAY(PAYMENT_TIME) <> 1 GROUP BY emp_id ) t " +
        "JOIN EMPLOYEE e ON e.EMP_ID = t.emp_id " +
        "JOIN DEPARTMENT d ON d.DEPARTMENT_ID = e.DEPARTMENT " +
        "WHERE (e.DEPARTMENT, t.SALARY) IN ( " +
        "SELECT e2.DEPARTMENT, MAX(t2.SALARY) " +
        "FROM ( SELECT emp_id, SUM(amount) AS SALARY FROM PAYMENTS " +
        "WHERE DAY(PAYMENT_TIME) <> 1 GROUP BY emp_id ) t2 " +
        "JOIN EMPLOYEE e2 ON e2.EMP_ID = t2.emp_id " +
        "GROUP BY e2.DEPARTMENT );";


Map<String, String> answerBody = new HashMap<>();
answerBody.put("finalQuery", finalQuery);


HttpHeaders headers2 = new HttpHeaders();
headers2.setContentType(MediaType.APPLICATION_JSON);
headers2.setBearerAuth(accessToken);   


HttpEntity<Map<String, String>> submitEntity =
        new HttpEntity<>(answerBody, headers2);


ResponseEntity<String> submitResponse =
        restTemplate.postForEntity(webhookUrl, submitEntity, String.class);

System.out.println("Submission Response: " + submitResponse.getBody());
	}
}
