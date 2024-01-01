package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.UserConversation;
import com.NewFeed.backend.modal.messaging.UserMessenger;
import com.NewFeed.backend.modal.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserMessengerRepository extends JpaRepository<UserMessenger,Long> {
    @Query("select " +
            " reciverMessenger , " +
            " count(message) ," +
            " profileImage " +
            "from UserMessenger messenger " +
                "inner join UserMessenger reciverMessenger on " +
                    " messenger.conversation = reciverMessenger.conversation and" +
                    " messenger.profile = ?1 and" +
                    " reciverMessenger.profile != ?1 and" +
                    " messenger.active = 1 and" +
                    " reciverMessenger.active = 1 " +
                "left join UnreadUserMessage message on " +
                    " message.messenger = messenger "+
                "left join Image profileImage on profileImage.imageableId = reciverMessenger.profile.id and" +
                    " profileImage.imageableType = 'UserProfile' and" +
                    " profileImage.active = 1 " +
               "group by " +
              " reciverMessenger ,profileImage")
    List<Object[]> findAllByUser(UserProfile profile);

    Optional<UserMessenger> findByProfileAndConversation(
            @Param("profile") UserProfile profile,
            @Param("conversation") UserConversation conversation
    );
    List<UserMessenger> findByConversation(UserConversation conversation);

}
