package com.oneassist.serviceplatform.commons.proxies;

import java.io.ByteArrayInputStream;
import java.util.Date;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.oneassist.serviceplatform.commons.utils.DateUtils;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("dhlproxy")
public class DHLProxy {

    private final Logger logger = Logger.getLogger(DHLProxy.class);

    @Value("${DHL_SFTP_HOST}")
    private String SFTPHOST;

    @Value("${DHL_SFTP_PORT}")
    private int SFTPPORT;

    @Value("${DHL_SFTP_USER}")
    private String SFTPUSER;

    @Value("${DHL_SFTP_PASSWORD}")
    private String SFTPPASS;

    @Value("${DHL_SFTP_DIRECTORY}")
    private String SFTPWORKINGDIR;
    private static String CHANNEL_NAME = "sftp";
    private static String DATE_FORMAT = "ddMMMyyyyHHmmss";
    private static String FILE_FORMAT = ".json";

    public void uploadSftpFile(String payload, ShipmentRequestDto shipmentDto) throws Exception {

        Channel channel = null;
        ChannelSftp channelSftp = null;
        Session session = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();

            channel = session.openChannel(CHANNEL_NAME);
            channel.connect();

            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(SFTPWORKINGDIR);

            channelSftp.put(new ByteArrayInputStream(payload.getBytes()), getDhlFileName(shipmentDto.getServiceRequestDetails().getRefPrimaryTrackingNo()));

            logger.error("payload published to DHL successfully" + payload);

        } 
        finally {
            if (channelSftp != null && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }

            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }

            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }

    }
    
    private String getDhlFileName(String srNumber){
        StringBuilder fileName = new StringBuilder();
        fileName.append(srNumber);
        fileName.append(DateUtils.toString(new Date(), DATE_FORMAT));
        fileName.append(FILE_FORMAT);

        return fileName.toString();
    }
}
