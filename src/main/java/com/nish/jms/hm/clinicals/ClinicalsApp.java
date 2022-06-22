package com.nish.jms.hm.clinicals;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.nish.jms.hm.model.Patient;

public class ClinicalsApp {

	public static void main(String[] args) throws NamingException, JMSException {

		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueueNish123");
		Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueueNish123");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();
			ObjectMessage objectMessage = jmsContext.createObjectMessage();
			Patient p = new Patient();
			p.setId(123);
			p.setName("ja");
			p.setInsuranceProvider("blue cross blue shield");
			p.setCopay(300d);
			p.setAmountToBePayed(500d);

			objectMessage.setObject(p);

			for (int i = 1; i <= 10; i++) {
				producer.send(requestQueue, objectMessage);
			}

			//JMSConsumer consumer = jmsContext.createConsumer(replyQueue);
			//MapMessage replyMessage = (MapMessage) consumer.receive(30000);
			//System.out.println("patient is " + replyMessage.getBoolean("eligible"));
		}
		;
	}

}
