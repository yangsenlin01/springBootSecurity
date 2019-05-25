package com.hand.security.core.validate.code;

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
@NoArgsConstructor
public class ImageCode {

    /**
     * 生成图片流，一个图片
     */
    private BufferedImage image;

    /**
     * 存入session的随机数
     */
    private String code;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 传入一个int类型的秒数，用来判断是否过期
     *
     * @param image
     * @param code
     * @param expireTime
     */
    public ImageCode(BufferedImage image, String code, int expireTime) {
        this.image = image;
        this.code = code;
        // 当前时间往后推expireTime秒
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

    /**
     * 全参构造器，用来生产验证码
     *
     * @param image
     * @param code
     * @param expireTime
     */
    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        this.image = image;
        this.code = code;
        this.expireTime = expireTime;
    }

    /**
     * 是否过期
     *
     * @return
     */
    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
