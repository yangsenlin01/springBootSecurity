package com.hand.web.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "sys_file")
public class File extends BaseEntity {

    /**
     * 附件id
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer fileId;

    /**
     * 分类id
     */
    private Integer attachmentId;

    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 附件路径
     */
    private String filePath;

    /**
     * 附件大小
     */
    private String fileSize;

    /**
     * 附件类型
     */
    private String fileType;

    /**
     * 附件上传时间
     */
    private Date uploadDate;

}
