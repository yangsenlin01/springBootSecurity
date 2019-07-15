package com.hand.web.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-7-15
 * @description
 */

@Getter
@Setter
@ToString
@Table(name = "sys_attachment")
public class Attachment extends BaseEntity {

    /**
     * 附件类型id
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Integer attachmentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 业务类型
     */
    private String sourceType;

    /**
     * 业务id
     */
    private String sourceKey;

    /**
     * 状态0->不可用，1->可用
     */
    private String status;

    /**
     * 有效期从
     */
    private Date startActiveDate;

    /**
     * 有效期至
     */
    private Date endActiveDate;

}
