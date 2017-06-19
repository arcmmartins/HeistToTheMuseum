package Datastructures;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Variables {
    
    /**
     * number of rooms
     */
    public static final int N       = 5; 

    /**
     * min/max paintings on room
     */
    public static final int Q[]     = {8,16};

    /**
     * distance Room-Site
     */
    public static final int D[]     = {15,30};

    /**
     * maximum separation limit between thieves
     */
    public static final int S       = 3;

    /** 
     * maximum displacement of the ordinary thieves
     */
    public static final int MD[]    ={2,6};

    /**
     * number of assault parties
     */
    public static final int NAP     =2;

    /**
     * number of thieves/Assault party
     */
    public static final int NTAP    =3;

    /**
     * number of thieves
     */
    public static final int M       = NAP*NTAP;
    
    /**
     * thief states
     */
    public class thief{

        /**
         * blocking state the ordinary thief is waken up by one of the
         * following operations of the master thief: prepareAssaultParty,
         * during heist operations, or sumUpResults, at the end of the heist 
         */
        public static final int outside                 =1000;         

        /**
         * transitional state with eventual waiting for the crawling
         * in movement to start, the first party member is waken up by
         * the operation sendAssaultParty of master thief the ordinary
         * thief proceeds until the target room at the museum is reached
         * and blocks if he can not generate a new increment of position
         * (before blocking, he wakes up the fellow party member that is
         * just behind him in the crawling queue, or the first one still
         * crawling, if he is the last) when blocking occurs, the ordinary
         * thief is waken up by the operation of crawlIn of a fellow party member 
         */
        public static final int crawling_inwards        =2000;

        /**
         * transitional state with eventual waiting for the crawling out
         * movement to start, the first party member is waken up by the
         * operation reverseDirection of the last party member to decide
         * to leave the room the ordinary thief proceeds until he reaches
         * the outside gathering site and blocks if he can not generate
         * a new increment of position (before blocking, he wakes up the
         * fellow party member that is just behind him in the crawling
         * queue, or the first one still crawling, if he is the last)
         * when blocking occurs, the ordinary thief is waken up by the
         * operation of crawlOut of a fellow party member
         */
        public static final int crawling_outwards       =4000;

        /**
         *  transitional state
         */
        public static final int atARoom                 =3000;
    }

    /**
     * master thief states
     */
    public class masterThief{

        /**
         * initial state (transitional)
         */
        public static final int planning_the_heist      =1000;         

        /**
         * transitional state with eventual waiting master thief proceeds
         * if the next operation is takeARest and blocks if it is one of
         * the other two and there is not a sufficient number of ordinary
         * thieves available (the totality for sumUpResults and enough to
         * create an assault party for prepareAssaultParty) when master thief
         * blocks, she is waken up by the operation amINeeded of an ordinary thief
         */
        public static final int assembling_a_group      =3000;

        /**
         * blocking state master thief is waken up by the operation
         * prepareExcursion of the last of the ordinary thieves to join the party
         */
        public static final int deciding_what_to_do     =2000;

        /**
         *  blocking state master thief is waken up by the operation
         * handACanvas of one of the assault party members returning from the museum 
         */
        public static final int presenting_the_report   =5000;

        /**
         * final state
         */
        public static final int  waiting_for_arrival    =4000;  
    }
    
    /**
     * Logging class
     */
    public class logger{

        /**
         *mask to log number of paintings in the room
         */
        public static final int NP      = 0;

        /**
         *mask to log distance to room
         */
        public static final int DT      = 1;

        /**
         *mask to log state
         */
        public static final int Stat    = 2;

        /**
         *mask to log in party/waiting to join status
         */
        public static final int S       = 3;

        /**
         *mask to log max displacement 
         */
        public static final int MD      = 4;

        /**
         *mask to log room id
         */
        public static final int RId     = 5;

        /**
         * mask to log id
         */
        public static final int Id      = 6;

        /**
         * mask to log position
         */
        public static final int Pos     = 7;

        /**
         * mask to log carrying canvas status
         */
        public static final int Cv      = 8;
        
    }
}



