package assignment2.DataStructures;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Variables implements java.io.Serializable{

    @Override
    public String toString() {
        return "Variables{" + "\nsockets=" + sockets + ", \nthief=" + thief + ", \nmasterThief=" + masterThief + ", \nlogger=" + logger + ", N=" + N + ", Q=" + Q + ", D=" + D + ", S=" + S + ", \nMD=" + MD + ", NAP=" + NAP + ", NTAP=" + NTAP + ", M=" + M + '}';
    }

    /**
     * Contains sockets info
     */
    public Sockets sockets = new Sockets();

    /**
     * contains thieves info
     */
    public Thief thief = new Thief();

    /**
     * contains master thief info
     */
    public MasterThief masterThief = new MasterThief();

    /**
     * contains logger info
     */
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
    public class Thief implements java.io.Serializable{

        @Override
        public String toString() {
            return "Thief{" + "outside=" + outside + ", crawling_inwards=" + crawling_inwards + ", crawling_outwards=" + crawling_outwards + ", atARoom=" + atARoom + '}';
        }
        
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
    public class MasterThief implements java.io.Serializable{

        @Override
        public String toString() {
            return "MasterThief{" + "planning_the_heist=" + planning_the_heist + ", assembling_a_group=" + assembling_a_group + ", \ndeciding_what_to_do=" + deciding_what_to_do + ", presenting_the_report=" + presenting_the_report + ", waiting_for_arrival=" + waiting_for_arrival + '}';
        }

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
     * Logger struct
     */
    public class Logger implements java.io.Serializable{

        @Override
        public String toString() {
            return "Logger{" + "NP=" + NP + ", DT=" + DT + ", Stat=" + Stat + ", S=" + S + ", \nMD=" + MD + ", RId=" + RId + ", Id=" + Id + ", Pos=" + Pos + ", Cv=" + Cv + '}';
        }

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
    public class Sockets implements java.io.Serializable{

        @Override
        public String toString() {
            return "Sockets{" + "Museum_socket=" + Museum_socket + ", Cs_socket=" + Cs_socket + ", Ccs_socket=" + Ccs_socket + ", AssaultParty0_socket=" + AssaultParty0_socket + ", AssaultPart1_socket=" + AssaultPart1_socket + ", \nLogger_socket=" + Logger_socket + ", MT_socket=" + MT_socket + ", OTip=" + OTip + ", OT0_socket=" + OT0_socket + ", OT1_socket=" + OT1_socket + ", OT2_socket=" + OT2_socket + ", \nOT3_socket=" + OT3_socket + ", OT4_socket=" + OT4_socket + ", OT5_socket=" + OT5_socket + '}';
        }
        /**
         * museum socket configuration
         */
        public  sk Museum_socket           = new sk("huehue",22146);
        /**
         * Cs socket configuration
         */
        public  sk Cs_socket               = new sk("localhost",22147);
        /**
         * Ccs socket configuration
         */
        public  sk Ccs_socket              = new sk("localhost",22141);
        /**
         * assault party 1 socket configuration
         */
        public  sk AssaultParty0_socket    = new sk("localhost",22142);
        /**
         * assault party 2 socket configuration
         */
        public  sk AssaultPart1_socket     = new sk("localhost",22143);
        /**
         * Logger socket configuration
         */
        public  sk Logger_socket           = new sk("localhost",22144);
        /**
         * master Thief socket configuration
         */
        public  sk MT_socket               = new sk("localhost",22145);
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
    public class sk implements java.io.Serializable{

        @Override
        public String toString() {
            return "sk{" + "ip=" + ip + ", port=" + port + '}';
        }

        /**
         * socket struct
         * @param ip the hostname
         * @param port the port
         */
        public sk(String ip, int port){
            this.ip=ip;
            this.port=port;
        }

        /**
         * hostname
         */
        public String ip;

        /**
         * port where to find
         */
        public int port;
    }
    
    
}



