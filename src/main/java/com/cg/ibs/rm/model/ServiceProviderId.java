package com.cg.ibs.rm.model;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ServiceProviderId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8053767533517376863L;
	@Column(name = "SPI")
	private BigInteger spi;
	@Column(name = "uci")
	private BigInteger uci;

	public ServiceProviderId() {
		super();
	}

	public ServiceProviderId(BigInteger spi, BigInteger uci) {
		super();
		this.spi = spi;
		this.uci = uci;
	}

	public BigInteger getSpi() {
		return spi;
	}

	public void setSpi(BigInteger spi) {
		this.spi = spi;
	}

	public BigInteger getUci() {
		return uci;
	}

	public void setUci(BigInteger uci) {
		this.uci = uci;
	}

	@Override
	public String toString() {
		return "ServiceProviderId [Spi=" + spi + ", uci=" + uci + "]";
	}

	 @Override
	    public boolean equals(Object obj) {
	        if (obj == this) {
	            return true;
	        }
	        if (obj == null || obj.getClass() != this.getClass()) {
	            return false;
	        }

	        ServiceProviderId spId = (ServiceProviderId) obj;
	        return (uci == spId.uci||(uci != null && uci.equals(spId.getUci())))
	                && (spi == spId.spi 
	                     || (spi != null && spi.equals(spId.getSpi())));
	    }
	    
	    @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result
	                + ((uci == null) ? 0 : uci.hashCode());
	        result = prime * result
	                + ((spi == null) ? 0 : spi.hashCode());
	        return result;
	    }
	    
	}
	

