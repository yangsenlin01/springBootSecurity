package com.hand.security.core.validate.code.image;

import com.hand.security.core.validate.code.ValidateCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-5-23
 * @description 验证码
 */

@Getter
@Setter
public class ImageCode extends ValidateCode {

    private BufferedImage image;

    public ImageCode(BufferedImage image, String code, int expireIn){
        super(code, expireIn);
        this.image = image;
    }

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime){
        super(code, expireTime);
        this.image = image;
    }
}
