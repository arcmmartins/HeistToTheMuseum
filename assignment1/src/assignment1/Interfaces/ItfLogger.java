package assignment1.Interfaces;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public interface ItfLogger {

    /**
     *method used by the ordinary thief to log
     * @param kind kind of parameter being logged
     * @param Tid id of the thief
     * @param val value of the parameter
     */
    void OTLog(int kind, int Tid, int val);

    /**
     * method used by the master thief to log
     * @param val value of the status
     */
    void MTLog(int val);

    /**
     * method used by the museum to log
     * @param kind kind of parameter being logged
     * @param Rid id of the room
     * @param val value of the parameter
     */
    void MuseumLog(int kind, int Rid, int val);

    /**
     * method used to log the report
     * @param total_paint
     */
    void finalReport(int total_paint);

    /**
     *method used by the assault party to log
     * @param kind kind of parameter being logged
     * @param APid id of the assault party
     * @param Eid  id of the thief
     * @param val  value of the parameter
     */
    void APLog(int kind, int APid, int Eid , int val);
}
