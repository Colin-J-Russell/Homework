package ComplexNumbers;
import java.util.InputMismatchException;
import java.util.Scanner;

// This is for calculating the complex roots of quadratic equations

public class MainApp {
	
	public static void main(String[] args) {
		if(Input.getInput()) {
			Roots roots = Solver.solve(Input.getA(), Input.getB(), Input.getC());
			Output.output(roots);
		}
		else {
			Output.output("Invalid input recieved");
		}
	}
}
class Solver{
	private final static double PRECISION = 0.00001;
	
	public static Roots solve(double a, double b, double c) {
		Roots roots = new Roots();
		if(Math.abs(a)<PRECISION) {
			if(Math.abs(b)<PRECISION) {
				if(Math.abs(c)<PRECISION) {
					roots.setFlag(ROOTTYPE.INFINITE);
				}
				else {
					roots.setFlag(ROOTTYPE.NONE);
				}
			}
			else {
				roots.setFlag(ROOTTYPE.ONELINEAR);
				roots.setRRoot1(-c/b);
			}
		}
		else {
			if(Math.abs(b*b-4.0*a*c)<PRECISION) {
				roots.setFlag(ROOTTYPE.ONEREAL);
				roots.setRRoot1(-b/(2.0*a));
			}
			else if((b*b-4.0*a*c)>0) {
				roots.setFlag(ROOTTYPE.TWOREAL);
				roots.setRRoot1((-b+Math.sqrt(b*b-4.0*a*c))/(2.0*a));
				roots.setRRoot2((-b-Math.sqrt(b*b-4.0*a*c))/(2.0*a));
			}
			else if((b*b-4.0*a*c)<0) {
				roots.setFlag(ROOTTYPE.TWOCOMPLEX);
				ComplexNumber c1 = new ComplexNumber((-b)/2.0*a, Math.sqrt(Math.abs(b*b-4.0*a*c))/(2.0*a));
				ComplexNumber c2 = new ComplexNumber((-b)/2.0*a, -1.0*Math.sqrt(Math.abs(b*b-4.0*a*c))/(2.0*a));
				roots.setCRoot1(c1);
				roots.setCRoot2(c2);
			}
		}
		return roots;
	}
}
class Roots{
	private double rroot1;
	private double rroot2;
	private ComplexNumber croot1;
	private ComplexNumber croot2;
	private ROOTTYPE rtype;
	
	public Roots() {
	}
	public ROOTTYPE getFlag(){
		return rtype;
	}
	public void setFlag(ROOTTYPE r) {
		rtype=r;
	}
	public ComplexNumber getCRoot1() {
		return croot1;
	}
	public void setCRoot1(ComplexNumber c) {
		croot1=c;
	}
	public ComplexNumber getCRoot2() {
		return croot2;
	}
	public void setCRoot2(ComplexNumber c) {
		croot2=c;
	}
	public double getRRoot1() {
		return rroot1;
	}
	public void setRRoot1(double r) {
		rroot1=r;
	}
	public double getRRoot2() {
		return rroot2;
	}
	public void setRRoot2(double r) {
		rroot2=r;
	}
	@Override
	public String toString() {
		switch(rtype) {
		case NONE:
			return "There are no Roots";
		case INFINITE:
			return "There are Infinite Roots";
		case TWOREAL:
			return "Equation is quadratic and has real roots " + rroot1 + " and " + rroot2;
		case ONEREAL:
			return "Equation is quadratic and has real root " + rroot1;
		case ONELINEAR:
			return "Equation is linear and has x intercept " + rroot1;
		case TWOCOMPLEX:
			return "Equation is quadratic and has complex roots " + croot1 + " and " + croot2;
		default:
			return "No Roottype";
		
		}
	}
}

enum ROOTTYPE{TWOCOMPLEX, TWOREAL, ONEREAL, ONELINEAR, INFINITE, NONE}

class ComplexNumber{
	private final static double PRECISION = 0.00001;
	private double re;
	private double im;
	
	public ComplexNumber() {
		
	}
	public ComplexNumber(double r, double i) {
		re=r;
		im=i;
	}
	public ComplexNumber(ComplexNumber c) {
		re=c.getRe();
		im=c.getIm();
	}
	public double getRe() {
		return re;
	}
	public double getIm() {
		return im;
	}
	@Override
	public String toString() {
		if(Math.abs(im)<PRECISION) {
			return re + "";
		}
		else if(Math.abs(re)<PRECISION) {
			return im + "i";
		}
		else if(im<0) {
			return re + " - " + (-1*im) + "i";
		}
		else {
			return re + " + " + im + "i";
		}
	}
	public boolean equals(Object o) {
		if(o instanceof ComplexNumber) {
			ComplexNumber c=(ComplexNumber)o;
			if(Math.abs(this.im-c.im)<PRECISION&&Math.abs(this.re-c.re)<PRECISION) {
				return true;
			}
			else return false;
		}
		else return false;
	}
}
class Input{
	private static double A;
	private static double B;
	private static double C;
	
	public static double getA() {
		return A;
	}
	public static double getB() {
		return B;
	}
	public static double getC() {
		return C;
	}
	public static boolean getInput() {
		boolean i = true;
		Scanner in = new Scanner(System.in);
		try {
			System.out.println("Please enter 3 numbers:");
			A=in.nextDouble();
			B=in.nextDouble();
			C=in.nextDouble();
		}
		catch(InputMismatchException e) {
			i=false;;
		}
		finally {
			in.close();
		}
		return i;
	}
}
class Output{
	public static void output(Roots r) {
		System.out.println(r);
	}
	public static void output(String s) {
		System.out.println(s);
	}
}