class TTable {
    private TTableEntry [] ArrayEntry;                //array for ttable which contains the information of a state 
	
	//Initialize all the entries
	TTable(){
	    ArrayEntry = new TTableEntry[10000];           //my current ttable has 4096 entries 
		for (int i =0; i<10000; ++i)
			ArrayEntry[i] = new TTableEntry();
	}
	
	//Find the index for the array of ttable
	private int getEntryIndex(long hashNum){
		return (int)(Math.abs(hashNum) % 10000);
	}
	
	//Store an entry to ttable
	public void store(TTableEntry entry){
		int index = getEntryIndex(entry.hash);
		ArrayEntry[index].hash = entry.hash;
		ArrayEntry[index].depth = entry.depth;
		ArrayEntry[index].a = entry.a;
		ArrayEntry[index].b = entry.b;
		ArrayEntry[index].value = entry.value;
	}
	
	//Look up an entry of the array with a given hash, return null if not found
	public TTableEntry getEntry(long hashNum){
		int index = getEntryIndex(hashNum);
		if (ArrayEntry[index].hash == hashNum)
			return ArrayEntry[index];
		else
			return null;
	}
	
}