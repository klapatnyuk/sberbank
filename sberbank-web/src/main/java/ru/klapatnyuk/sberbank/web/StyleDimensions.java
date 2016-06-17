package ru.klapatnyuk.sberbank.web;

import static ru.klapatnyuk.sberbank.web.StylePostfix.*;

/**
 * @author klapatnyuk
 */
public interface StyleDimensions {

    Integer HEIGHT_S_RAW = 24;
    Integer HEIGHT_M_RAW = 28;
    Integer HEIGHT_L_RAW = 40;
    Integer HEIGHT_XL_RAW = 50;

    String HEIGHT_S = "" + HEIGHT_S_RAW + PX;
    String HEIGHT_M = "" + HEIGHT_M_RAW + PX;
    String HEIGHT_L = "" + HEIGHT_L_RAW + PX;
    String HEIGHT_XL = "" + HEIGHT_XL_RAW + PX;

    Integer WIDTH_XXS_RAW = 16;
    Integer WIDTH_XS_RAW = 90;
    Integer WIDTH_S_RAW = 125;
    Integer WIDTH_M_RAW = 150;
    Integer WIDTH_L_RAW = 200;
    Integer WIDTH_XL_RAW = 250;
    Integer WIDTH_XXL_RAW = 350;
    Integer WIDTH_XXXL_RAW = 700;

    String WIDTH_XXS = "" + WIDTH_XXS_RAW + PX;
    String WIDTH_XS = "" + WIDTH_XS_RAW + PX;
    String WIDTH_S = "" + WIDTH_S_RAW + PX;
    String WIDTH_M = "" + WIDTH_M_RAW + PX;
    String WIDTH_L = "" + WIDTH_L_RAW + PX;
    String WIDTH_XL = "" + WIDTH_XL_RAW + PX;
    String WIDTH_XXL = "" + WIDTH_XXL_RAW + PX;
    String WIDTH_XXXL = "" + WIDTH_XXXL_RAW + PX;

    String WIDTH = WIDTH_M;

    Integer VIDEO_WIDTH_RAW = 704;
    Integer VIDEO_HEIGHT_RAW = 576;

    String VIDEO_WIDTH = "" + VIDEO_WIDTH_RAW + PX;
    String VIDEO_HEIGHT = "" + VIDEO_HEIGHT_RAW + PX;

    Integer SEPERATOR_HEIGHT_RAW = 1;

    String SEPARATOR_HEIGHT = "" + SEPERATOR_HEIGHT_RAW + PX;

    String WINDOW_WIDTH = "412px";
    String WINDOW_WIDTH_L = "425px";
    String WINDOW_WIDTH_XL = "500px";
    String WINDOW_WIDTH_XXL = "600px";

    String WINDOW_VIDEO_WIDTH = "" + (VIDEO_WIDTH_RAW + 25 * 4) + PX;

    String WINDOW_HEIGHT = "400px";
    String WINDOW_HEIGHT_L = "500px";
}