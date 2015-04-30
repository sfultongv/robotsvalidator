package controllers;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import model.RobotResults;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.robots;


public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result robots(String site, String agent, String path) {
        RobotResults robotResults = RobotResults.NO_RESULTS;
        if (StringUtils.isNotBlank(site) && StringUtils.isNotBlank(agent) && StringUtils.isNotBlank(path)) {
            try {
                HttpResponse response = Request.Get(site).execute().returnResponse();
                int responseCode = response.getStatusLine().getStatusCode();
                if (responseCode == 200) {
                    InputStream robotsStream = response.getEntity().getContent();
                    robotResults = new RobotResults(site, agent, path, robotsStream);
                } else {
                    robotResults = new RobotResults("Robots request returned " + responseCode);
                }

            } catch (IOException e) {
                robotResults = new RobotResults(e.toString());
            }
        }

        return ok(robots.render(robotResults));
    }
}
