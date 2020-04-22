package distributedsystems.demo.controllers;

import distributedsystems.demo.dto.NodeDTO;
import distributedsystems.demo.model.MulticastPublisher;
import distributedsystems.demo.model.NamingServer;
import distributedsystems.demo.model.Node;
import distributedsystems.demo.model.XMLParser;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
        return namingServer.findFile(name);
    }
   /* @GetMapping("/getfile/{name}")
    public String getFile(@PathVariable String name) {
        Node node = new Node("Node1", "111,111,111,111");
        xmlParser.addToXML(node.getName(),node.getIp());
        node.addFile(new File("C:\\Users\\Thomas\\Desktop\\nodebox.txt"));
        namingServer.addNode(node);
        return namingServer.findFile(name);
    }*/


    @PostMapping("/add")
    public int addNode(@RequestBody NodeDTO nodeDTO) {
        Node node = new Node(nodeDTO.getName(), nodeDTO.getIp());
        namingServer.addNode(node);
        return namingServer.getSize();
    }
    @GetMapping("/")
    public HashMap getNodes()
    {
        return namingServer.getNodes();
    }
    @PostMapping("/add")
    public HashMap addNode(@RequestBody Map<String,String> body)
    {
        String name = body.get("name");
        String ip  = body.get("ip");
        Node node = new Node(name, ip);
        xmlParser.addToXML(node.getName(),node.getIp());
        namingServer.addNode(node);
        return namingServer.getNodes();
    }
    @DeleteMapping("/delete/{id}")
    public HashMap removeNode(@PathVariable Integer id)
    {
        namingServer.removeNode(id);
        return namingServer.getNodes();
    }


}
