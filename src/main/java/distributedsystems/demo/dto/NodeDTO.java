package distributedsystems.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Thomas Somers
 * @version 1.0 20/04/2020 10:40
 */

@Data
public class NodeDTO {

    @NotEmpty
    private String name;

    @NotEmpty
    private String ip;

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }
}
