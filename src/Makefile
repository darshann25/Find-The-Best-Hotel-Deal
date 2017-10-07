#Make environment
CC=javac
EX=java

# Files
SOURCE_FILES=	BestHotelDeal.java 
OBJECT_FILES= BestHotelDeal.class \ Hotel.class \ HotelDB.class
EXECUTABLE_FILES= BestHotelDeal

# Productions
all : tripadvisor-best-hotel-deal run


tripadvisor-best-hotel-deal : $(SOURCE_FILES)
	$(CC) $(SOURCE_FILES)

run :
	$(EX) $(EXECUTABLE_FILES) -h
 

clean : 
	rm -f *.class
