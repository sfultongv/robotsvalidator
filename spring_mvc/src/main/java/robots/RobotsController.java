package robots;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import robots.model.RobotResults;

@Controller
public class RobotsController {


    @RequestMapping("/robots")
    public String robots
        (@RequestParam(value="site", required=false) String site
        ,@RequestParam(value="agent", required=false) String agent
        ,@RequestParam(value="path", required=false) String path
        ,Model model
    ) { 
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

        model.addAttribute("result", robotResults);
        return "robots";
    }
}
