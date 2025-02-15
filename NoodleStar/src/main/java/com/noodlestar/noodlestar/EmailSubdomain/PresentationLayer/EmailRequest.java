package com.noodlestar.noodlestar.EmailSubdomain.PresentationLayer;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {

    private String to;
    private String subject;
    private String body;

}
