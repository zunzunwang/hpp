package fr.tse.fi2.hpp.labs.beans;

public class GridPoint {
	/**
	 * Coordinates on the Grid
	 */
	int x, y;

	public GridPoint(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	@Override
	public boolean equals(Object o)   
    {   
        if (this == o)   
        {   
            return true;   
        }   
        if (o.getClass() == GridPoint.class)   
        {   
        	GridPoint n = (GridPoint)o;
            return ((n.x==x)&&(n.y==y));   
        }   
        return false;   
    }   
	@Override   
    // récrire la fonction hashCode.  
    public int hashCode()   
    {   
		final int prime = 233;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
        return result;   
    }  
	

}
