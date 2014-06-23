package net.aconite.affina.espinterface.dao.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class StageScriptChunker
{
    private String trackAlias;
    private String pan;
    private String psn;
    private Timestamp expDate;
    private String appType;
    private String appVersion;
    private String busFundAlias;
    private Timestamp stageStartDate;
    private Timestamp stageEndDate;
    private BigDecimal stageMaxcount;
    //--------------
    private BigDecimal scriptableManifestOID;
    private BigDecimal stageScriptFilterOID;
    private String cardId;
    private BigDecimal scopeOID;
    //--------------
    private String filterTrackId;
    private String scopeName;
    private String appId;
    //----------------
    private BigDecimal stageScriptDetailOID;
    private BigDecimal busFuncOID;
    private BigDecimal manifestAppOID;
    private BigDecimal softCardOID;
    private BigDecimal appOID;
    

    public String getTrackAlias()
    {
        return trackAlias;
    }

    public void setTrackAlias(String trackAlias)
    {
        this.trackAlias = trackAlias;
    }

    public String getPan()
    {
        return pan;
    }

    public void setPan(String pan)
    {
        this.pan = pan;
    }

    public String getPsn()
    {
        return psn;
    }

    public void setPsn(String psn)
    {
        this.psn = psn;
    }

    public Timestamp getExpDate()
    {
        return expDate;
    }

    public void setExpDate(Timestamp expDate)
    {
        this.expDate = expDate;
    }

    public String getAppType()
    {
        return appType;
    }

    public void setAppType(String appType)
    {
        this.appType = appType;
    }

    public String getAppVersion()
    {
        return appVersion;
    }

    public void setAppVersion(String appVersion)
    {
        this.appVersion = appVersion;
    }

    public String getBusFundAlias()
    {
        return busFundAlias;
    }

    public void setBusFundAlias(String busFundAlias)
    {
        this.busFundAlias = busFundAlias;
    }

    public Timestamp getStageStartDate()
    {
        return stageStartDate;
    }

    public void setStageStartDate(Timestamp stageStartDate)
    {
        this.stageStartDate = stageStartDate;
    }

    public Timestamp getStageEndDate()
    {
        return stageEndDate;
    }

    public void setStageEndDate(Timestamp stageEndDate)
    {
        this.stageEndDate = stageEndDate;
    }

    public BigDecimal getStageMaxcount()
    {
        return stageMaxcount;
    }

    public void setStageMaxcount(BigDecimal stageMaxcount)
    {
        this.stageMaxcount = stageMaxcount;
    }

    public BigDecimal getScriptableManifestOID()
    {
        return scriptableManifestOID;
    }

    public void setScriptableManifestOID(BigDecimal scriptableManifestOID)
    {
        this.scriptableManifestOID = scriptableManifestOID;
    }

    public BigDecimal getStageScriptFilterOID()
    {
        return stageScriptFilterOID;
    }

    public void setStageScriptFilterOID(BigDecimal stageScriptFilterOID)
    {
        this.stageScriptFilterOID = stageScriptFilterOID;
    }

    public BigDecimal getScopeOID()
    {
        return scopeOID;
    }

    public void setScopeOID(BigDecimal scopeOID)
    {
        this.scopeOID = scopeOID;
    }

    public String getFilterTrackId()
    {
        return filterTrackId;
    }

    public void setFilterTrackId(String filterTrackId)
    {
        this.filterTrackId = filterTrackId;
    }

    public String getScopeName()
    {
        return scopeName;
    }

    public void setScopeName(String scopeName)
    {
        this.scopeName = scopeName;
    }

    public BigDecimal getStageScriptDetailOID()
    {
        return stageScriptDetailOID;
    }

    public void setStageScriptDetailOID(BigDecimal stageScriptDetailOID)
    {
        this.stageScriptDetailOID = stageScriptDetailOID;
    }

    public BigDecimal getBusFuncOID()
    {
        return busFuncOID;
    }

    public void setBusFuncOID(BigDecimal busFuncOID)
    {
        this.busFuncOID = busFuncOID;
    }

    public BigDecimal getManifestAppOID()
    {
        return manifestAppOID;
    }

    public void setManifestAppOID(BigDecimal manifestAppOID)
    {
        this.manifestAppOID = manifestAppOID;
    }

    public BigDecimal getSoftCardOID()
    {
        return softCardOID;
    }

    public void setSoftCardOID(BigDecimal softCardOID)
    {
        this.softCardOID = softCardOID;
    }

    public BigDecimal getAppOID()
    {
        return appOID;
    }

    public void setAppOID(BigDecimal appOID)
    {
        this.appOID = appOID;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getCardId()
    {
        return cardId;
    }

    public void setCardId(String cardId)
    {
        this.cardId = cardId;
    }

    @Override
    public String toString()
    {
         StringBuilder sb = new StringBuilder();
        sb.append("\nTrackAlias           : ").append(trackAlias);
        sb.append("\nExpDate              : ").append(expDate);
        sb.append("\nAppType              : ").append(appType);
        sb.append("\nAppVersion           : ").append(appVersion);
        sb.append("\nBusFundAlias         : ").append(busFundAlias);
        sb.append("\nStageStartDate       : ").append(stageStartDate);
        sb.append("\nStageEndDate         : ").append(stageEndDate);
        sb.append("\nStageMaxcount        : ").append(stageMaxcount);
        sb.append("\nScriptableManifestOID: ").append(scriptableManifestOID);
        sb.append("\nStageScriptFilterOID : ").append(stageScriptFilterOID);
        sb.append("\nCardId               : ").append(cardId);
        sb.append("\nScopeOID             : ").append(scopeOID);
        sb.append("\nFilterTrackId        : ").append(filterTrackId);
        sb.append("\nScopeName            : ").append(scopeName);
        sb.append("\nAppId                : ").append(appId);
        sb.append("\nStageScriptDetailOID : ").append(stageScriptDetailOID);
        sb.append("\nBusFuncOID           : ").append(busFuncOID);
        sb.append("\nManifestAppOID       : ").append(manifestAppOID);
        sb.append("\nSoftCardOID          : ").append(softCardOID);
        sb.append("\nAppOID               : ").append(appOID);        
        sb.append("\n");
        return sb.toString();
       
    }
    
    
}