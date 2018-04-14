import java.io.*;
import java.util.*;

public class ObliviousTransfer{

	public long power(long x, long y, long p)
    {
        // Initialize result
        long res = 1;     
        
        // Update x if it is more  
        // than or equal to p
        x = x % p; 
     
        while (y > 0)
        {
            // If y is odd, multiply x
            // with result
            if((y & 1)==1)
                res = (res * x) % p;
     
            // y must be even now
            // y = y / 2
            y = y >> 1; 
            x = (x * x) % p; 
        }
        return res;
    }

	public static void main(String[] args) {

	Alice alice = new Alice();
	Bob bob = new Bob();

	//calc X
	alice.calculateX();
	bob.e = alice.e;
	bob.N = alice.N;

	// Bit generator & mesage selection by Bob:
	bob.selectBit();
	bob.selectMessage(alice.x0,alice.x1);

	//generate k
	bob.generateK();
	long v =bob.getV();

	//At alice's end
	alice.calculateK(v);
	long nm1 = alice.getMessage1();
	long nm2 = alice.getMessage2();

	//at bob's end
	bob.printMessage(nm1,nm2);
	
	}
}
class Alice{
	//choosing the RSA pair:
	public long N = 4699 , e = 13 ;
	private long d = 349;

	public int x0, x1;
	public long v , k0 ,k1;

	long m0 = 87 , m1 = 69 ;

	public void calculateX(){
	//random values x0,x1.
	 x0 = (int)(Math.random()*100);
	 x1 = (int)(Math.random()*100);
	 System.out.println("x0:"+x0);
	 System.out.println("x1:"+x1);
	}	

	public void calculateK(long v){
		this.v = v;
		ObliviousTransfer obv = new ObliviousTransfer();
		 k0 = (obv.power(v-x0,d,N))  ;
		 k1 = (obv.power(v-x1,d,N))  ;
		System.out.println("k0"+k0);
		System.out.println("k1"+k1);
	}
	public long getMessage1(){
		System.out.println("k0+m0:"+(m0+k0));
		return m0+k0;
	}
	public long getMessage2(){
		System.out.println("k1+m1:"+(m1+k1));
		return m1+k1;
	}

}
class Bob{

	private int bit , xb , k ;

	public long v, e ,N ;

	public void selectBit(){
		bit = 1;
	}

	public void selectMessage(int x0,int x1){
		if(bit==0)
			xb = x0;
		else
			xb = x1;
		System.out.println("xb:"+xb);
	}

	public void generateK(){
		k = (int)(Math.random()*100);
		System.out.println("k:"+k);
	}

	public long getV(){
		System.out.println("k"+k+" e"+e+" N:"+N);
		ObliviousTransfer obv = new ObliviousTransfer();
		 v = (xb + (obv.power(k,e,N))) % N ;
		 System.out.println("v"+v);
		 return v;
	}

	public void printMessage(long nm0, long nm1){
		System.out.println("Bit"+bit);
		if(bit==0)
			System.out.println(nm0-k);
		else
			System.out.println(nm1-k);
		System.out.println("****");
		}
}
