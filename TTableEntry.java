class TTableEntry {
    public long hash;              //each entry for ttable will contain a long hash number
	public int depth;              //a depth search
	public int a;                  //a value a (awful)
	public int b;                  //a value b (best) 
	public int value;              //a value for the state

	//Initialize all to zero.
    TTableEntry(){
	    hash = 0;
		depth = 0;
        a = 0;
        b = 0;
        value = 0;
	}
}