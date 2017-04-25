package assignment2.DataStructures;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Variables {
    public Sockets sockets = new Sockets();
    public Thief thief = new Thief();
    public MasterThief masterThief = new MasterThief();
    public Logger logger = new Logger();
    /**
     * number of rooms
     */
    public final int N       = 5; 

    /**
     * min/max paintings on room
     */
    public final int Q[]     = {8,16};

    /**
     * distance Room-Site
     */
    public final int D[]     = {15,30};

    /**
     * maximum separation limit between thieves
     */
    public final int S       = 3;

    /** 
     * maximum displacement of the ordinary thieves
     */
    public final int MD[]    ={2,6};

    /**
     * number of assault parties
     */
    public final int NAP     =2;

    /**
     * number of thieves/Assault party
     */
    public final int NTAP    =3;

    /**
     * number of thieves
     */
    public final int M       = NAP*NTAP;
    
    /**
     * Thief states
     */
    public class Thief{

        /**
         * blocking state the ordinary Thief is waken up by one of the
 following operations of the master Thief: prepareAssaultParty,
 during heist operations, or sumUpResults, at the end of the heist 
         */
        public final int outside                 =1000;         

        /**
         * transitional state with eventual waiting for the crawling
 in movement to start, the first party member is waken up by
 the operation sendAssaultParty of master Thief the ordinary
 Thief proceeds until the target room at the museum is reached
 and blocks if he can not generate a new increment of position
 (before blocking, he wakes up the fellow party member that is
 just behind him in the crawling queue, or the first one still
 crawling, if he is the last) when blocking occurs, the ordinary
 Thief is waken up by the operation of crawlIn of a fellow party member 
         */
        public final int crawling_inwards        =2000;

        /**
         * transitional state with eventual waiting for the crawling out
 movement to start, the first party member is waken up by the
 operation reverseDirection of the last party member to decide
 to leave the room the ordinary Thief proceeds until he reaches
 the outside gathering site and blocks if he can not generate
 a new increment of position (before blocking, he wakes up the
 fellow party member that is just behind him in the crawling
 queue, or the first one still crawling, if he is the last)
 when blocking occurs, the ordinary Thief is waken up by the
 operation of crawlOut of a fellow party member
         */
        public final int crawling_outwards       =4000;

        /**
         *  transitional state
         */
        public final int atARoom                 =3000;
    }

    /**
     * master Thief states
     */
    public class MasterThief{

        /**
         * initial state (transitional)
         */
        public final int planning_the_heist      =1000;         

        /**
         * transitional state with eventual waiting master Thief proceeds
 if the next operation is takeARest and blocks if it is one of
 the other two and there is not a sufficient number of ordinary
 thieves available (the totality for sumUpResults and enough to
 create an assault party for prepareAssaultParty) when master Thief
 blocks, she is waken up by the operation amINeeded of an ordinary Thief
         */
        public final int assembling_a_group      =3000;

        /**
         * blocking state master Thief is waken up by the operation
 prepareExcursion of the last of the ordinary thieves to join the party
         */
        public final int deciding_what_to_do     =2000;

        /**
         *  blocking state master Thief is waken up by the operation
 handACanvas of one of the assault party members returning from the museum 
         */
        public final int presenting_the_report   =5000;

        /**
         * final state
         */
        public final int  waiting_for_arrival    =4000;  
    }
    
    /**
     *
     */
    public class Logger{

        /**
         *mask to log number of paintings in the room
         */
        public final int NP      = 0;

        /**
         *mask to log distance to room
         */
        public final int DT      = 1;

        /**
         *mask to log state
         */
        public final int Stat    = 2;

        /**
         *mask to log in party/waiting to join status
         */
        public final int S       = 3;

        /**
         *mask to log max displacement 
         */
        public final int MD      = 4;

        /**
         *mask to log room id
         */
        public final int RId     = 5;

        /**
         * mask to log id
         */
        public final int Id      = 6;

        /**
         * mask to log position
         */
        public final int Pos     = 7;

        /**
         * mask to log carrying canvas status
         */
        public final int Cv      = 8;
        
    }
    
    /**
     *socket configuration
     */
    public class Sockets{
        /**
         * museum socket configuration
         */
        public  sk Museum_socket           = new sk("localhost",22140);
        /**
         * Cs socket configuration
         */
        public  sk Cs_socket               = new sk("localhost",22150);
        /**
         * Ccs socket configuration
         */
        public  sk Ccs_socket              = new sk("localhost",22160);
        /**
         * assault party 1 socket configuration
         */
        public  sk AssaultParty0_socket    = new sk("localhost",22170);
        /**
         * assault party 2 socket configuration
         */
        public  sk AssaultPart1_socket     = new sk("localhost",22180);
        /**
         * Logger socket configuration
         */
        public  sk Logger_socket           = new sk("localhost",22190);
        /**
         * master Thief socket configuration
         */
        public  sk MT_socket               = new sk("localhost",22100);
        /**
         * ordinary thieves ip 
         */
        public  String OTip                = "localhost";
        /**
         * OT1 socket configuration
         */
        public  sk OT0_socket              = new sk(OTip,22141);
        /**
         * OT2 socket configuration
         */
        public  sk OT1_socket              = new sk(OTip,22142);
        /**
         * OT3 socket configuration
         */
        public  sk OT2_socket              = new sk(OTip,22143);
        /**
         * OT4 socket configuration
         */
        public  sk OT3_socket              = new sk(OTip,22144);
        /**
         * OT5 socket configuration
         */
        public  sk OT4_socket              = new sk(OTip,22145);
        /**
         * OT6 socket configuration
         */
        public  sk OT5_socket              = new sk(OTip,22146);
        
    
    }
    
    /**
     *socket definition class
     */
    public class sk{
        public sk(String ip, int port){
            this.ip=ip;
            this.port=port;
        }
        public String ip;
        public int port;
    }
    
    
}



