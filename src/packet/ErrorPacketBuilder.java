package packet;

import java.net.DatagramPacket;
import java.net.InetAddress;
import helpers.Conversion;
import types.ErrorType;
import types.ModeType;
import types.RequestType;

/**
 * @author Team 3
 *
 * This class is used to construct the Error packets used for the TFTP system.
 */
public class ErrorPacketBuilder extends PacketBuilder {

	private ErrorType mErrorType;
	
	public ErrorPacketBuilder(InetAddress addressOfHost, int destPort) {
		super(addressOfHost, destPort, RequestType.ERROR);
	}

	public ErrorPacketBuilder(DatagramPacket inDatagramPacket) {
		super(inDatagramPacket);
		
		deconstructPacket(inDatagramPacket);
	}

	@Override
	public DatagramPacket buildPacket() {
		throw new IllegalArgumentException("You must provide a ErrorType to build a ERROR packet!");
	}
	
	public DatagramPacket buildPacket(ErrorType errorType) {
		this.mErrorType = errorType;
		byte[] errorHeaderBytes = this.mRequestType.getHeaderByteArray();
		byte[] errorCodeBytes = Conversion.shortToBytes(errorType.getErrorCodeShort());
		byte[] errorMessageBytes = errorType.getErrorMessageString().getBytes();
		int bufferLength = errorHeaderBytes.length + 
						   errorCodeBytes.length + 
						   errorMessageBytes.length + 1;
		this.mBuffer = new byte[bufferLength];
		System.arraycopy(errorHeaderBytes, 0, this.mBuffer, 0, errorHeaderBytes.length);
		System.arraycopy(errorCodeBytes, 0, this.mBuffer, errorHeaderBytes.length, errorCodeBytes.length);
		System.arraycopy(errorMessageBytes, 0, this.mBuffer, errorHeaderBytes.length + errorCodeBytes.length, errorMessageBytes.length);
		this.mBuffer[bufferLength - 1] = 0; // Null terminating zero
		return new DatagramPacket(this.mBuffer, this.mBuffer.length, this.mInetAddress, this.mDestinationPort);
	}

	@Override
	public void deconstructPacket(DatagramPacket inDatagramPacket) {
		// Only using this to deconstruct to send back to sender
		setRequestTypeFromBuffer(this.mBuffer);
		if(this.mRequestType == RequestType.ERROR) {
			this.mErrorType = ErrorType.matchErrorByNumber(this.mBuffer[3]);
		}
	}
	
	@Override 
	protected byte[] getRequestTypeHeaderByteArray() {
		return RequestType.ERROR.getHeaderByteArray();
	}
	
	/* (non-Javadoc)
	 * @see packet.PacketBuilder#getDataBuffer()
	 */
	public byte[] getDataBuffer() {
		return this.mBuffer;
	}

	@Override
	public void setBlockNumber(short i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getBlockNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFilename(String fileName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMode(ModeType mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ModeType getMode() {
		// TODO Auto-generated method stub
		return null;
	}

}
