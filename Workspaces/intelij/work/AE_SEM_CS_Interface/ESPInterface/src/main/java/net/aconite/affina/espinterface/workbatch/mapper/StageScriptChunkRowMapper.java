package net.aconite.affina.espinterface.workbatch.mapper;

import net.aconite.affina.espinterface.dao.model.StageScriptChunker;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class StageScriptChunkRowMapper implements RowMapper<StageScriptChunker>
{

    @Override
    public StageScriptChunker mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        StageScriptChunker ssChunker = new StageScriptChunker();
        
        ssChunker.setTrackAlias(rs.getString("trackAlias"));
        ssChunker.setPan(rs.getString("pan"));
        ssChunker.setPsn(rs.getString("psn"));
        ssChunker.setExpDate(rs.getTimestamp("expDate"));
        ssChunker.setAppType(rs.getString("appType"));
        ssChunker.setAppVersion(rs.getString("appVersion"));
        ssChunker.setBusFundAlias(rs.getString("busFuncAlias"));
        ssChunker.setStageStartDate(rs.getTimestamp("stageStartDate"));
        ssChunker.setStageEndDate(rs.getTimestamp("stageEndDate"));
        ssChunker.setStageMaxcount(rs.getBigDecimal("stageMaxcount"));
        
        ssChunker.setScriptableManifestOID(rs.getBigDecimal("scriptableManifestOID"));
        ssChunker.setStageScriptFilterOID(rs.getBigDecimal("stageScriptFilterOID"));
        ssChunker.setCardId(rs.getString("cardId"));
        ssChunker.setScopeOID(rs.getBigDecimal("scopeOID"));
        
        ssChunker.setFilterTrackId(rs.getString("filterTrackId"));
        ssChunker.setScopeName(rs.getString("scopeName"));
        ssChunker.setAppId(rs.getString("appId"));
        
        ssChunker.setStageScriptDetailOID(rs.getBigDecimal("stageScriptDetailOID"));
        ssChunker.setBusFuncOID(rs.getBigDecimal("busFuncOID"));
        ssChunker.setManifestAppOID(rs.getBigDecimal("manifestAppOID"));
        ssChunker.setSoftCardOID(rs.getBigDecimal("softCardOID"));
        ssChunker.setAppOID(rs.getBigDecimal("appOID"));
        

        return ssChunker;
    }
}