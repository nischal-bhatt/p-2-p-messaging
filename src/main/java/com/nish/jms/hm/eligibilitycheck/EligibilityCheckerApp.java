package com.nish.jms.hm.eligibilitycheck;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.nish.jms.hm.eligibilitycheck.listeners.EligibilityCheckListener;
import com.nish.jms.hm.model.Patient;

public class EligibilityCheckerApp {

	public static void main(String[] args) throws NamingException, InterruptedException
	{
		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue)initialContext.lookup("queue/requestQueueNish123");
		
		try( ActiveMQConnectionFactory cf= new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext())
		{
			
			JMSConsumer consumer1 = jmsContext.createConsumer(requestQueue);
			JMSConsumer consumer2 = jmsContext.createConsumer(requestQueue);
			
			//consumer.setMessageListener(new EligibilityCheckListener()); 
			 
			for (int i = 1; i<=10; i+=2)
			{
				System.out.println("consumer1: " + consumer1.receive());
				System.out.println("consumer2: " + consumer2.receive());
			}
			
			//Thread.sleep(10000);
		};
	}
	
}
