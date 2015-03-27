package com.spydotechcorps.hwfar.database;

/**
 * Created by INGENIO on 3/8/2015.
 */
public class saveDistance {
    private int _id;
    private String _desc;
    private String _distance;

    public void Distances() {

    }

    /*public void Distances(int id, String desc, String distance) {
        this._id = id;
        this._desc = desc;
        this._distance = distance;
    }*/

    public void Distances(String desc, String distance) {
        this._desc = desc;
        this._distance = distance;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setdesc(String desc) {
        this._desc = desc;
    }

    public String getdesc() {
        return this._desc;
    }

    public void setdistance(String distance) {
        this._distance = distance;
    }

    public String getdistance() {
        return this._distance;
    }


}
