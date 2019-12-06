package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ErrorCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FileTransferInfoRecordVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FileTransferInfoVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.GochainAddressVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.UuidNameDateVO;

public interface FileTransferMapper {
	@Select(
		"SELECT"
			+ " CAST( _file_transfer_uuid AS text ) AS fileTransferUuid,"
			+ " CAST( _wallet_address_uuid AS text ) AS walletAddressUuid,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM \"user\".insert_start_file_transfer_session_record( #{ userId } , DECODE( #{ walletAddress } , 'hex'), #{ receiverLoginName } )"
	)
	FileTransferInfoVO insertFileTransferSessionRecord(
		@Param("userId") long userId,
		@Param("walletAddress") String walletAddress,
		@Param("receiverLoginName") String receiverLoginName
	);

	@Select(
		"SELECT"
			+ " CAST( file_transfer_uuid AS text ) AS uuid,"
			+ " sender_login_name AS name,"
			+ " file_transfer_create_dt AS create"
			+ " FROM gochain.file_transfer_waiting_for_receiver_address( #{ userId } )"
	)
	UuidNameDateVO[] retrieveSessionWaitingForReceiverAddress(
		@Param("userId") long userId
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.set_file_transfer_active( #{ userId }, #{ fileTransferUuid } )"
	)
	ErrorCodeMessageResponse setFileTransferAsActive(
		@Param("userId") long userId,
		@Param("fileTransferUuid") UUID fileTransferUuid
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.set_file_transfer_inactive( #{ userId }, #{ fileTransferUuid } )"
	)
	ErrorCodeMessageResponse setFileTransferInactive(
		@Param("userId") long userId,
		@Param("fileTransferUuid") UUID fileTransferUuid
	);

	@Select(
		"SELECT"
			+ " #{ fileTransferUuid } AS fileTransferUuid,"
			+ " _sender_login_name AS senderLoginName,"
			+ " CAST( _sender_wallet_address_uuid AS text ) AS senderWalletAddressUuid,"
			+ " _sender_wallet_address AS senderWalletAddress,"
			+ " _receiver_login_name AS receiverLoginName,"
			+ " CAST( _receiver_wallet_address_uuid AS text ) AS receiverWalletAddressUuid,"
			+ " _receiver_wallet_address AS receiverWalletAddress,"
			+ " _smart_contract_address AS smartContractAddress,"
			+ " _funding_1_rec_pubkey_status AS funding1RecPubkeyStatus,"
			+ " _funding_1_rec_pubkey_transaction_hash AS funding1RecPubkeyTransactionHash,"
			+ " _funding_2_send_docinfo_status AS funding2SendDocinfoStatus,"
			+ " _funding_2_send_docinfo_transaction_hash AS funding2SendDocinfoTransactionHash,"
			+ " _funding_3_rec_final_status AS funding3RecFinalStatus,"
			+ " _funding_3_rec_final_transaction_hash AS funding3RecFinalTransactionHash,"
			+ " _file_transfer_is_active AS fileTransferIsActive,"
			+ " _file_transfer_create_dt AS fileTransferCreate,"
			+ " _file_transfer_update_dt AS fileTransferUpdate,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.get_user_file_transfer_info( #{ userId }, #{ fileTransferUuid } )"
	)
	FileTransferInfoVO getUserFileTransferInfo(
		@Param("userId") long userId,
		@Param("fileTransferUuid") UUID fileTransferUuid
	);
	@Select(
		"SELECT"
			+ " CAST( file_transfer_uuid AS text ) AS fileTransferUuid,"
			+ " sender_login_name AS senderLoginName,"
			+ " CAST( sender_wallet_address_uuid AS text ) AS senderWalletAddressUuid,"
			+ " sender_wallet_address AS senderWalletAddress,"
			+ " receiver_login_name AS receiverLoginName,"
			+ " CAST( receiver_wallet_address_uuid AS text ) AS receiverWalletAddressUuid,"
			+ " receiver_wallet_address AS receiverWalletAddress,"
			+ " smart_contract_address AS smartContractAddress,"
			+ " funding_1_rec_pubkey_status AS funding1RecPubkeyStatus,"
			+ " funding_1_rec_pubkey_transaction_hash AS funding1RecPubkeyTransactionHash,"
			+ " funding_2_send_docinfo_status AS funding2SendDocinfoStatus,"
			+ " funding_2_send_docinfo_transaction_hash AS funding2SendDocinfoTransactionHash,"
			+ " funding_3_rec_final_status AS funding3RecFinalStatus,"
			+ " funding_3_rec_final_transaction_hash AS funding3RecFinalTransactionHash,"
			+ " file_transfer_is_active AS fileTransferIsActive,"
			+ " file_transfer_create_dt AS fileTransferCreate,"
			+ " file_transfer_update_dt AS fileTransferUpdate"
			+ " FROM gochain.get_file_transfer_sessions_for_user( #{ userId } )"
		)
			FileTransferInfoRecordVO[] getFileTransferSessionsForUser(
			@Param("userId") long userId
		);

	@Select(
		"SELECT"
			+ " CAST( _wallet_address_uuid AS text ) AS gochainAddress,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.set_file_transfer_receiver_address( #{ userId } , #{ fileTransferUuid } , DECODE( #{ walletAddress } , 'hex'))"
	)
	GochainAddressVO setReceiverAddress(
		@Param("userId") long userId,
		@Param("fileTransferUuid") UUID fileTransferUuid,
		@Param("walletAddress") String walletAddress
	);

	@Select(
		"SELECT"
			+ " _is_available AS value,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.set_file_transfer_step_availability("
				+ " #{ fileTransferUuid },"
				+ " DECODE( #{ walletAddress }, 'hex' ),"
				+ " CAST( #{ step } AS gochain.tp_funding_step )"
			+ " )"
	)
	BooleanVO setFileTransferStepAvailability(
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
				+ " FROM gochain.file_transfer_set_funding_step_pending("
					+ " #{ fileTransferUuid },"
					+ " DECODE( #{ walletAddress }, 'hex'),"
					+ " CAST( #{ step } AS gochain.tp_funding_step ),"
				+ " )"
		)
		ErrorCodeMessageResponse fileTransferSetTransferStepPending(
			@Param("fileTransferUuid") UUID fileTransferUuid,
			@Param("walletAddress") String walletAddress,
			@Param("step") String step
		);

	@Select(
			"SELECT"
				+ " _funding_id AS id,"
				+ " _error_code AS errorCode,"
				+ " _error_message AS errorMessage"
				+ " FROM gochain.file_transfer_set_funding_step_completed("
					+ " #{ fileTransferUuid },"
					+ " DECODE( #{ walletAddress }, 'hex'),"
					+ " CAST( #{ step } AS gochain.tp_funding_step ),"
					+ " DECODE( #{ transactionHash }, 'hex' )"
				+ " )"
		)
		IdVO fileTransferSetTransferStepCompleted(
			@Param("fileTransferUuid") UUID fileTransferUuid,
			@Param("walletAddress") String walletAddress,
			@Param("step") String step,
			@Param("transactionHash") String transactionHash
		);
	//These are the functions used to support dbGateway
	@Select(
	        "SELECT" +
	        " gochain.file_transfer_set_contract_address(" +
	        " #{ fileTransferUuid }," +
	        " DECODE( #{fileTransferContractAddress }, 'hex' )" +
	        " )"
	        )
	        BooleanVO fileTransferSetContractAddress(
	                @Param("fileTransferUuid") UUID fileTransferUuid,
	                @Param("fileTransferContractAddress") String contractAddress
	        );
	
	
}
