package distributedsystems.demo.controllers;

import distributedsystems.demo.model.NamingServer;
import distributedsystems.demo.model.Node;
import distributedsystems.demo.model.XMLParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @author Thomas Somers
 * @version 1.0 26/03/2020 9:55
 */

@RestController
public class NamingServerController {

    private NamingServer namingServer = new NamingServer();
    private XMLParser xmlParser = new XMLParser();


    @GetMapping("/getfile/{name}")
    public String getBalance(@PathVariable String name) {
        Node node = new Node("Node1", "111,111,111,111");
        xmlParser.addToXML(node.getName(),node.getIp());
        node.addFile(new File("C:\\Users\\Thomas\\Desktop\\nodebox.txt"));
        namingServer.addNode(node);
        return namingServer.findFile(name);
    }
}
