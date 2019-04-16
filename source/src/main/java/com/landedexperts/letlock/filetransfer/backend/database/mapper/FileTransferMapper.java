package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.result.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.result.ConsumeVO;
import com.landedexperts.letlock.filetransfer.backend.database.result.ErrorCodeMessageVO;
import com.landedexperts.letlock.filetransfer.backend.database.result.GochainAddressVO;
import com.landedexperts.letlock.filetransfer.backend.database.result.IdVO;

public interface FileTransferMapper {
	@Select(
		"SELECT"
			+ " CAST( _file_transfer_uuid AS text ) AS fileTransferUuid,"
			+ " CAST( _wallet_address_uuid AS text ) AS walletAddressUuid,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM \"user\".consume_start_file_transfer( #{ userId } , DECODE( #{ walletAddress } , 'hex'), #{ receiverLoginName } )"
	)
	ConsumeVO consumeStartFileTransfer(
		@Param("userId") int userId,
		@Param("walletAddress") String walletAddress,
		@Param("receiverLoginName") String receiverLoginName
	);

	@Select(
		"SELECT"
			+ " CAST( _wallet_address_uuid AS text ) AS gochainAddress,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.file_transfer_set_receiver_address( #{ userId } , #{ fileTransferUuid } , DECODE( #{ walletAddress } , 'hex'))"
	)
	GochainAddressVO setReceiverAddress(
		@Param("userId") int userId,
		@Param("fileTransferUuid") UUID fileTransferUuid,
		@Param("walletAddress") String walletAddress
	);

	@Select(
		"SELECT"
			+ " _is_available AS value,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.file_transfer_is_step_available( #{ fileTransferUuid }, #{ walletAddress }, #{ step } )"
	)
	BooleanVO fileTransferIsStepAvailable(
		@Param("fileTransferUuid") UUID fileTransferUuid,
		@Param("walletAddress") String walletAddress,
		@Param("step") String step
	);

	@Select(
		"SELECT"
			+ " _is_available AS value,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.file_transfer_is_step_pending( #{ fileTransferUuid }, #{ walletAddress }, #{ step } )"
	)
	BooleanVO fileTransferIsStepPending(
		@Param("fileTransferUuid") UUID fileTransferUuid,
		@Param("walletAddress") String walletAddress,
		@Param("step") String step
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.file_transfer_set_contract_address( #{ fileTransferUuid }, #{ fileTransferAddress } )"
	)
	ErrorCodeMessageVO fileTransferSetContractAddress(
		@Param("fileTransferUuid") UUID fileTransferUuid,
		@Param("fileTransferAddress") String fileTransferAddress
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.file_transfer_set_funding_step_pending("
				+ " #{ fileTransferUuid },"
				+ " DECODE( #{ walletAddress }, 'hex'),"
				+ " #{ step }"
			+ " )"
	)
	ErrorCodeMessageVO fileTransferSetTransferStepPending(
		@Param("fileTransferUuid") UUID fileTransferUuid,
		@Param("walletAddress") String walletAddress,
		@Param("step") String step
	);

	@Select(
		"SELECT"
			+ " _transfer_id AS id,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.file_transfer_set_funding_step_completed("
				+ " #{ fileTransferUuid },"
				+ " DECODE( #{ walletAddress }, 'hex'),"
				+ " #{ step },"
				+ " DECODE( #{ transactionHash }, 'hex' )"
			+ " )"
	)
	IdVO fileTransferSetTransferStepCompleted(
		@Param("fileTransferUuid") UUID fileTransferUuid,
		@Param("walletAddress") String walletAddress,
		@Param("step") String step,
		@Param("transactionHash") String transactionHash
	);
}
