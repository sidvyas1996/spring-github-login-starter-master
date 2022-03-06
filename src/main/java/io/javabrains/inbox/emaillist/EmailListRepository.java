package io.javabrains.inbox.emaillist;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface EmailListRepository extends CassandraRepository<EmailListItems, EmailListItemKey> {
    
    List<EmailListItems> findAllByKey_IdAndKey_Label(String id, String label);
}
