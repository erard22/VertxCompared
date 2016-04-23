package ch.erard22.vertx.compared;

import ch.erard22.vertx.compared.common.User;
import io.vertx.core.AbstractVerticle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href='https://twitter.com/michelerard'>michelerard</a>
 */
public class GetVerticle extends AbstractVerticle {


    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(HttpVerticle.GET, message -> {
            String body = (String) message.body();

            List<User> list = new ArrayList<>();

            User u = new User();
            u.setId("123");
            u.setName("Erard");
            u.setFirstname("Michel");
            list.add(u);

            message.reply(list);
        });
    }
}
