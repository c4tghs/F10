package distributedsystems.demo.controllers;

import distributedsystems.demo.dto.NodeDTO;
import distributedsystems.demo.model.NamingServer;
import distributedsystems.demo.model.Node;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Thomas Somers
 * @version 1.0 26/03/2020 9:55
 */

@RestController
public class NamingServerController {

    private NamingServer namingServer = new NamingServer();


    @GetMapping("/findFile/{fileName}")
    public String findFile(@PathVariable String fileName) {
        return namingServer.findFile(fileName);
    }

    @GetMapping("/ip/{hash}")
    public String getHash(@PathVariable int hash) {
        return namingServer.getNodes().get(hash);
    }


    @PostMapping("/add")
    public int addNode(@RequestBody NodeDTO nodeDTO) {
        Node node = new Node(nodeDTO.getName(), nodeDTO.getIp());
        namingServer.addNode(node);
        return namingServer.getSize();
    }
    @PostMapping("/file/hash")
    public void sendFileHash(@RequestBody int hash) {
//        namingServer
    }
}
