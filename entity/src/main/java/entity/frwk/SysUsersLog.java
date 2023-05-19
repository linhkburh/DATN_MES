package entity.frwk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYS_USER_LOG", uniqueConstraints = @UniqueConstraint(columnNames = "ID"))
public class SysUsersLog implements java.io.Serializable {

    private String id;
    private String userId;
    private String action;
    private String recordId;
    private Date modifyTime;
    private String detail;

    public void UserLog() {
    }

    public void UserLog(String recordId, SysUsers userId) {
        this.recordId = recordId;
        this.userId = userId.getUsername();
    }
    
	public void UserLog(String id, SysUsers userId, String action, String recordId) {
        this.id = id;
        this.userId = userId.getUsername();
        this.action = action;
        this.recordId = recordId;
    }
    
    @Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    @Column(name = "USER_ID", nullable = false, length = 40)
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "ACTION", nullable = false, length = 100)
    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Column(name = "RECORD_ID", length = 40)
    public String getRecordId() {
        return this.recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    @Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_TIME", nullable = false, length = 7)
    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Column(name = "DETAIL", nullable = false, length = 4000)
    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
}
