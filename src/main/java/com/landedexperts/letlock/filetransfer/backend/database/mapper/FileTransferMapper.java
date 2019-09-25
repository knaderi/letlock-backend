package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.FileTransferSessionVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.ErrorCodeMessageVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.FileTransferInfoVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.GochainAddressVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.UuidNameDateVO;

public interface FileTransferMapper {
	@Select(
		"SELECT"
			+ " CAST( _file_transfer_uuid AS text ) AS fileTransferUuid,"
			+ " CAST( _wallet_address_uuid AS text ) AS walletAddressUuid,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM \"user\".insert_start_file_transfer_session_record( #{ userId } , DECODE( #{ walletAddress } , 'hex'), #{ receiverLoginName } )"
	)
	FileTransferSessionVO insertFileTransferSessionRecord(
		@Param("userId") int userId,
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
		@Param("userId") int userId
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.set_file_transfer_to_active( #{ userId }, #{ fileTransferUuid } )"
	)
	ErrorCodeMessageVO setFileTransferAsActive(
		@Param("userId") int userId,
		@Param("fileTransferUuid") UUID fileTransferUuid
	);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.file_transfer_deactivate( #{ userId }, #{ fileTransferUuid } )"
	)
	ErrorCodeMessageVO setFileTransferInactive(
		@Param("userId") int userId,
		@Param("fileTransferUuid") UUID fileTransferUuid
	);

	@Select(
		"SELECT"
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
			+ " FROM gochain.get_user_file_transfer_Info( #{ userId }, #{ fileTransferUuid } )"
	)
	FileTransferInfoVO getUserFileTransferInfo(
		@Param("userId") int userId,
		@Param("fileTransferUuid") UUID fileTransferUuid
	);

	@Select(
		"SELECT"
			+ " CAST( _wallet_address_uuid AS text ) AS gochainAddress,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM gochain.set_file_transfer_receiver_address( #{ userId } , #{ fileTransferUuid } , DECODE( #{ walletAddress } , 'hex'))"
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
		ErrorCodeMessageVO fileTransferSetTransferStepPending(
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
}
