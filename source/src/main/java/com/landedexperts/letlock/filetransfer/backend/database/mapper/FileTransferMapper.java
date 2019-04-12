package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.result.ConsumeResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.WalletAddressResult;

public interface FileTransferMapper {
	@Select("SELECT"
			+ " _wallet_address_uuid AS walletAddress, _error_code AS errorCode, _error_message AS errorMessage"
			+ " FROM gochain.file_transfer_set_receiver_address( #{ userId } , #{ fileTransferUuid } , DECODE( #{ walletAddress } , 'hex'))")
	WalletAddressResult setReceiverAddress(@Param("userId") int userId, @Param("fileTransferUuid") UUID fileTransferUuid, @Param("walletAddress") String walletAddress);

	@Select("SELECT"
			+ " _file_transfer_uuid AS fileTransferUuid, _wallet_address_uuid AS walletAddressUuid, _error_code AS errorCode, _error_message AS errorMessage"
			+ " FROM \"user\".consume_start_file_transfer( #{ userId } , DECODE( #{ walletAddress } , 'hex'), #{ receiverLoginName } )")
	ConsumeResult consumeStartFileTransfer(@Param("userId") int userId, @Param("walletAddress") String walletAddress, @Param("receiverLoginName") String receiverLoginName);
}
