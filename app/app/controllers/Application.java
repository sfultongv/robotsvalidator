package controllers;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRulesParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.robots;


public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result robots(String site, String agent, String path) {
        String returnString = "";
        if (StringUtils.isNotBlank(site) && StringUtils.isNotBlank(agent) && StringUtils.isNotBlank(path)) {
            try {
                byte[] robotsContent = Request.Get(site)
                    .execute().returnContent().asBytes();
                SimpleRobotRulesParser robotParser = new SimpleRobotRulesParser();
                BaseRobotRules robotRules =
                    robotParser.parseContent(site, robotsContent, "text/plain", agent);
                returnString = robotRules.isAllowed(path) ? "allowed" : "not allowed";

            } catch (IOException e) {
                returnString = e.toString();
            }
        }

        return ok(robots.render(site, agent, path, returnString));
    }
}
