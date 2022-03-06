package io.javabrains.inbox;

import java.nio.file.Path;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.inbox.email.Email;
import io.javabrains.inbox.email.EmailRepository;
import io.javabrains.inbox.emaillist.EmailListItemKey;
import io.javabrains.inbox.emaillist.EmailListItems;
import io.javabrains.inbox.emaillist.EmailListRepository;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;

@SpringBootApplication
@RestController
public class InboxApp {

	@Autowired FolderRepository folderRepository;
	@Autowired EmailListRepository emailListRepository;
	@Autowired EmailRepository emailRepository;
	public static void main(String[] args) {
		SpringApplication.run(InboxApp.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@PostConstruct
	public void init(){

		folderRepository.save(new Folder("sidvyas1996", "Inbox", "blue"));
		folderRepository.save(new Folder("sidvyas1996", "Sent", "green"));
		folderRepository.save(new Folder("sidvyas1996", "Drafts", "yellow"));

		for(int i=0;i<10;i++)
		{
			 EmailListItemKey key = new EmailListItemKey();
			 key.setId("sidvyas1996");
			 key.setLabel("Inbox");
			 key.setTimeUUID(Uuids.timeBased());

			 EmailListItems emailListItems= new EmailListItems();
			 emailListItems.setKey(key);
			 emailListItems.setTo(Arrays.asList("vedVyas24","Asda", "dpsa"));
			 emailListItems.setSubject("New world "+i);
			 emailListItems.setUnread(true);

			emailListRepository.save(emailListItems);

			Email email = new Email();
			email.setId(key.getTimeUUID());
			email.setTo(emailListItems.getTo());
			email.setFrom("sidvyas1996");
			email.setSubject(emailListItems.getSubject());
			email.setBody("Body "+ i);
			emailRepository.save(email);
		}
	}

}
