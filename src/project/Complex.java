package project;

public class Complex {
	private double Re;
	private double Im;
	
	public Complex(double Re, double Im) {
		this.Re = Re;
		this.Im = Im;
	}
	
	public Complex(Complex z) {
		this.Re = z.getRe();
		this.Im = z.getIm();
	}
	
	public double getRe() {
		return this.Re;
	}
	
	public double getIm() {
		return this.Im;
	}
	
	public void setRe(double Re) {
		this.Re = Re;
	}
	
	public void setIm(double Im) {
		this.Im = Im;
	}
	
	public void print() {
		System.out.println("Wartosc to " + Double.toString(getRe()) + " + " + Double.toString(getIm())+   "i");
	}


	public static Complex Add(Complex z1, Complex z2) {
		double Re1 = z1.getRe();
		double Re2 = z2.getRe();
		
		double Im1 = z1.getIm();
		double Im2 = z2.getIm();
		
		Complex ret = new Complex(Re1 + Re2,Im1 + Im2);
		
		return ret;
	}
	
	public static Complex Sub(Complex z1, Complex z2) {
		double Re1 = z1.getRe();
		double Re2 = z2.getRe();
		
		double Im1 = z1.getIm();
		double Im2 = z2.getIm();
		
		Complex ret = new Complex(Re1 - Re2,Im1 - Im2);
		
		return ret;
	}
	
	public static Complex Mul(Complex z1, Complex z2) {
		double Re1 = z1.getRe();
		double Re2 = z2.getRe();
		
		double Im1 = z1.getIm();
		double Im2 = z2.getIm();
		
		Complex ret = new Complex(Re1 * Re2 - Im1 * Im2, Re1 * Im2 + Im1 * Re2);
		
		return ret;
	}
	
	public static Complex Div(Complex z1, Complex z2) {
		double Re1 = z1.getRe();
		double Im1 = z1.getIm();

		double Re2 = z2.getRe();
		double Im2 = z2.getIm();
		
		double ReRet = Re1 * Re2 + Im1 * Im2;
		double ImRet = Im1 * Re2 - Re1 * Im2;
		
		ReRet/=(Re2 * Re2 + Im2 * Im2);
		ImRet/=(Re2 * Re2 + Im2 * Im2);
		
		Complex ret = new Complex(ReRet, ImRet);
		
		return ret;
	}

	public static double getModulus(Complex z1) {
		double ret;
		ret = z1.getRe() * z1.getRe();
		ret +=(z1.getIm() * z1.getIm());
		ret = Math.sqrt(ret);
		return ret;
	}
	
	
	public void Add(Complex z2) {
		double Re1 = this.getRe();
		double Re2 = z2.getRe();
		
		double Im1 = this.getIm();
		double Im2 = z2.getIm();
		
		setRe(Re1 + Re2);
		setIm(Im1 + Im2);
	}
	
	public void Sub(Complex z2) {
		double Re1 = this.getRe();
		double Re2 = z2.getRe();
		
		double Im1 = this.getIm();
		double Im2 = z2.getIm();
		
		setRe(Re1 - Re2);
		setIm(Im1 - Im2);
	}
	
	public void Mul(Complex z2) {
		double Re1 = this.getRe();
		double Re2 = z2.getRe();
		
		double Im1 = this.getIm();
		double Im2 = z2.getIm();
		
		
		setRe(Re1 * Re2 - Im1 * Im2);
		setIm(Re1 * Im2 + Im1 * Re2);
	}
	
	public void Div(Complex z2) {
		double Re1 = this.getRe();
		double Re2 = z2.getRe();
		
		double Im1 = this.getIm();
		double Im2 = z2.getIm();
		
		
		double ReRet = Re1 * Re2 + Im1 * Im2;
		double ImRet = Im1 * Re2 - Re1 * Im2;
		
		ReRet/=(Re2 * Re2 + Im2 * Im2);
		ImRet/=(Re2 * Re2 + Im2 * Im2);
		
		setRe(ReRet);
		setIm(ImRet);
	}

	public void Div(double x2) {
		double Re1 = this.getRe();
		double Im1 = this.getIm();
		
		setRe(Re1/x2);
		setIm(Im1/x2);
		
	}
	
	public double getModulus() {
		double ret;
		ret = this.getRe() * this.getRe();
		ret +=(this.getIm() * this.getIm());
		ret = Math.sqrt(ret);
		return ret;
	}
}

