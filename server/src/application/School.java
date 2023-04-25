package application;

import java.io.Serializable;

public class School implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
    private String password;
    private double[] modelcoeffs= {0.0,0.0,0.0,0.0};

    public School(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public School(String email, String password,double[] coeffs) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

	public double[] getModelcoeffs() {
		return modelcoeffs;
	}

	public void setModelcoeffs(double[] ds) {
		this.modelcoeffs = ds;
	}

}