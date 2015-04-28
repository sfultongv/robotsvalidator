package controllers;

import java.io.IOException;

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

    public static Result robots() {
        String returnString;
        String crawlerName = "googlebot";
        String url = "http://dev.rwjf.velir.com/robots.txt";
        try {
            byte[] robotsContent = Request.Get(url)
                .execute().returnContent().asBytes();
            SimpleRobotRulesParser robotParser = new SimpleRobotRulesParser();
            BaseRobotRules robotRules =
                robotParser.parseContent(url, robotsContent, "text/plain", crawlerName);
            String testUrl = "http://dev.rwjf.velir.com/content/rwjf/en.html";
            returnString = String.format("A request to %s by agent %s is allowed: %b", testUrl, crawlerName, robotRules.isAllowed(testUrl));

        } catch (IOException e) {
            returnString = e.toString();
        }

        return ok(robots.render(returnString));
    }
}
