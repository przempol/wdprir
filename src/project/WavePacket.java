package project;


public class WavePacket {
	public double time;
	public int size;
	
	// for wave packet data 
	public double [] psiRe;
	public double[] psiIm;
	
	// hamiltonian
	private double[] hamRe; 
	private double [] hamIm;
	
	// rest of wave packet data 
	private double [] position;
	private double [] potential;
	
	public WavePacket (int n) {
		this.size = Constants.TABLE_LEN*n + 1;
		
		this.psiRe = new double[size];
		this.psiIm = new double[size];
		this.hamRe = new double[size];
		this.hamIm = new double[size];
		
    	this.position = new double[size];
    	this.potential = new double[size];
    	
    	for (int ii = 0; ii < size; ii++) {
    		// for chart purpose 
    		double x = (double) ii/ (double) (size - 1);
    		this.time = 0.;
    		position[ii] = x;
    		
    		// finally initialize wave packet
    		double tmp = - (x - Constants.X0) * (x - Constants.X0) / (2 * Constants.SIGMA0 * Constants.SIGMA0 ) ;
    		tmp = Math.exp(tmp);
    		psiRe[ii] = Math.cos(Constants.K0 * x) * tmp;
    		psiIm[ii] = Math.sin(Constants.K0 * x) * tmp;
			
    		// setting up potential
			if ((x > 0.5 - 0.05) && (x < 0.5 + 0.05)) {
	    		potential[ii] = Constants.U0/10;
			}else {
	    		potential[ii] = 0.;
			}
    	}
    	//hamiltonian
		for(int ii=1; ii<size-1; ii++){
			hamRe[ii]=calculateHamiltonian(psiRe[ii-1], psiRe[ii], psiRe[ii+1], potential[ii]);
			hamIm[ii]=calculateHamiltonian(psiIm[ii-1], psiIm[ii], psiIm[ii+1], potential[ii]);
		}
	}

	public double calculateHamiltonian(double psi_, double psi0, double psi1, double potential){
		return ((this.size * this.size)  * (-psi1-psi_+2*psi0)/2) + potential * psi0;
	}
	
    public double[][] getAmplitudeData() {
		double[] yData = new double[this.size];
		double[] potData = new double[this.size];
		for (int ii = 0; ii < size; ii++) {
			yData[ii] = Math.sqrt(psiRe[ii] * psiRe[ii] + psiIm[ii] * psiIm[ii]);
//			yData[ii] = psiIm[ii];
			if (potential[ii] < 0.1) {
				potData[ii] = 0;
			}else {
				potData[ii] = 0.5;
			}
		}
		return new double[][] {position, yData , potData, psiIm, psiRe};
    }
    
    // for serial computations
	public void updateWavePacket() {
		for(int ii=0;ii<size;ii++)		psiRe[ii]=psiRe[ii]+hamIm[ii]*Constants.dt/2.;
		
		for(int jj=1;jj<size-1;jj++)	hamRe[jj]=calculateHamiltonian(psiRe[jj-1], psiRe[jj], psiRe[jj+1], potential[jj]);
		
		for(int ii=0;ii<size;ii++)		psiIm[ii]=psiIm[ii]-hamRe[ii]*Constants.dt;
		
		for(int jj=1;jj<size-1;jj++)	hamIm[jj]=calculateHamiltonian(psiIm[jj-1], psiIm[jj], psiIm[jj+1], potential[jj]);
			
		for(int ii=0;ii<size;ii++)		psiRe[ii]=psiRe[ii]+hamIm[ii]*Constants.dt/2.; 
	}
	
	// for parallel computations
	public void updateWavePacketParallel(int type, int start, int end) {
		if (type==0){
			for(int ii=start;ii<end;ii++) {
				psiRe[ii]=psiRe[ii]+hamIm[ii]*Constants.dt/2.;
			}
		}else if (type==1) {
			for(int jj=start;jj<end;jj++){
					if (start != 0 && end != size) {
						hamRe[jj]=calculateHamiltonian(psiRe[jj-1], psiRe[jj], psiRe[jj+1], potential[jj]);
					}
				}
		}else if (type==2) {
			for(int ii=start;ii<end;ii++) {
				psiIm[ii]=psiIm[ii]-hamRe[ii]*Constants.dt;
			}
		}else if(type == 3) {
			for(int jj=start;jj<end;jj++){
				if (start != 0 && end != size) {
					hamIm[jj]=calculateHamiltonian(psiIm[jj-1], psiIm[jj], psiIm[jj+1], potential[jj]);
				}
			}
		}else if(type == 4) {
			for(int ii=start;ii<end;ii++) {
				psiRe[ii]=psiRe[ii]+hamIm[ii]*Constants.dt/2.; 
			}
		}else System.out.println("blad");
	}
    
}
