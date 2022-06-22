package com.nish.jms.hm.eligibilitycheck.listeners;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.nish.jms.hm.model.Patient;

public class EligibilityCheckListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage)message;
		try(ActiveMQConnectionFactory cf= new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			
			InitialContext initialContext = new InitialContext();
			Queue replyQueue = (Queue)initialContext.lookup("queue/replyQueueNish123");
			MapMessage replyMessage = jmsContext.createMapMessage();
			
			Patient p = (Patient)objectMessage.getObject();
			
			if (p.getInsuranceProvider().equals("blue cross blue shield"))
			{
				if (p.getCopay() < 40 && p.getAmountToBePayed() < 1000)
				{
					replyMessage.setBoolean("eligible", true);
				}
			}else
			{
				replyMessage.setBoolean("eligible", false);
			}
			
			JMSProducer producer = jmsContext.createProducer();
			producer.send(replyQueue, replyMessage);
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
