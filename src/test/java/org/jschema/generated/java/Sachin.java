package org.jschema.generated.java;
import java.util.*;

import org.jschema.parser.*;
public class Sachin{
  private Map<String, Object> _fields = new HashMap<String, Object>();

  public static Sachin parse(String jsonString){
    Sachin newSachin = new Sachin();
    Map<String, Object> jsonObject = (Map) new Parser(jsonString).parse();
    Iterator it = jsonObject.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry)it.next();
      if(pair.getValue() instanceof Map){
        Object obj = makeObject(newSachin, (String)pair.getKey(), (Map)pair.getValue());
        newSachin._fields.put((String) pair.getKey(), obj);
      }
      else if(pair.getValue() instanceof List){
        List list = makeList(newSachin, (String)pair.getKey(), (List)pair.getValue());
        newSachin._fields.put((String) pair.getKey(), list);
      }
      else{
        newSachin._fields.put((String) pair.getKey(), pair.getValue());
      }
    }
    return newSachin;
  }
  private static Object makeObject(Sachin newSachin, String key, Map value){
    if(key.equals("query")){
      Sachin.Query q = newSachin.new Query();
      q = (Query) makeQuery(q, key, value);
      return q;
    }
    return null;
  }
  private static Object makeQuery(Query newQuery, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      if(pair.getKey().toString().equals("results")){
        Query.Results r = newQuery.new Results();
        r = (Query.Results) makeResults(r, (String) pair.getKey(), (Map) pair.getValue());
        newQuery._fields.put((String) pair.getKey(), r);
      }
      else newQuery._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newQuery;
  }
  private static Object makeResults(Query.Results newResults, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      if(pair.getKey().toString().equals("quote")){
        Query.Results.Quote q = newResults.new Quote();
        q = (Query.Results.Quote) makeQuote(q, (String) pair.getKey(), (Map) pair.getValue());
        newResults._fields.put((String) pair.getKey(), q);
      }
      else newResults._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newResults;
  }
  private static Object makeQuote(Query.Results.Quote newQuote, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newQuote._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newQuote;
  }
  private static List makeList(Sachin newSachin, String key, List value){
    List<Object> list = new ArrayList<>();
    for(int i = 0; i < value.size(); i++) {
      if(value.get(i) instanceof Map){
        Object result = makeObject(newSachin, key, (Map) value.get(i));
        list.add(result);
      }
      else if(value.get(i) instanceof List){
        List result = makeList(newSachin, key, (List) value.get(i));
        list.add(result);      }
      else{
        list.add(value.get(i));
      }
    }
    return list;
  }
  public java.lang.String toJSON(){return _fields.toString();}

  public Query getquery(){return (Query) _fields.get("query");}
  public void setquery(Query query){_fields.put("query", query);}

  public class Query{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.Integer getcount(){return (java.lang.Integer) _fields.get("count");}
    public void setcount(java.lang.Integer count){_fields.put("count", count);}

    public java.util.Date getcreated(){return (java.util.Date) _fields.get("created");}
    public void setcreated(java.util.Date created){_fields.put("created", created);}

    public java.lang.String getlang(){return (java.lang.String) _fields.get("lang");}
    public void setlang(java.lang.String lang){_fields.put("lang", lang);}

    public Results getresults(){return (Results) _fields.get("results");}
    public void setresults(Results results){_fields.put("results", results);}

    public class Results{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public Quote getquote(){return (Quote) _fields.get("quote");}
      public void setquote(Quote quote){_fields.put("quote", quote);}

      public class Quote{
        private Map<String, Object> _fields = new HashMap<String, Object>();

        public java.lang.String getsymbol(){return (java.lang.String) _fields.get("symbol");}
        public void setsymbol(java.lang.String symbol){_fields.put("symbol", symbol);}

        public java.lang.String getAsk(){return (java.lang.String) _fields.get("Ask");}
        public void setAsk(java.lang.String Ask){_fields.put("Ask", Ask);}

        public java.lang.String getAverageDailyVolume(){return (java.lang.String) _fields.get("AverageDailyVolume");}
        public void setAverageDailyVolume(java.lang.String AverageDailyVolume){_fields.put("AverageDailyVolume", AverageDailyVolume);}

        public java.lang.String getBid(){return (java.lang.String) _fields.get("Bid");}
        public void setBid(java.lang.String Bid){_fields.put("Bid", Bid);}

        public java.lang.Object getAskRealtime(){return (java.lang.Object) _fields.get("AskRealtime");}
        public void setAskRealtime(java.lang.Object AskRealtime){_fields.put("AskRealtime", AskRealtime);}

        public java.lang.Object getBidRealtime(){return (java.lang.Object) _fields.get("BidRealtime");}
        public void setBidRealtime(java.lang.Object BidRealtime){_fields.put("BidRealtime", BidRealtime);}

        public java.lang.String getBookValue(){return (java.lang.String) _fields.get("BookValue");}
        public void setBookValue(java.lang.String BookValue){_fields.put("BookValue", BookValue);}

        public java.lang.String getChange_PercentChange(){return (java.lang.String) _fields.get("Change_PercentChange");}
        public void setChange_PercentChange(java.lang.String Change_PercentChange){_fields.put("Change_PercentChange", Change_PercentChange);}

        public java.lang.String getChange(){return (java.lang.String) _fields.get("Change");}
        public void setChange(java.lang.String Change){_fields.put("Change", Change);}

        public java.lang.Object getCommission(){return (java.lang.Object) _fields.get("Commission");}
        public void setCommission(java.lang.Object Commission){_fields.put("Commission", Commission);}

        public java.lang.String getCurrency(){return (java.lang.String) _fields.get("Currency");}
        public void setCurrency(java.lang.String Currency){_fields.put("Currency", Currency);}

        public java.lang.Object getChangeRealtime(){return (java.lang.Object) _fields.get("ChangeRealtime");}
        public void setChangeRealtime(java.lang.Object ChangeRealtime){_fields.put("ChangeRealtime", ChangeRealtime);}

        public java.lang.Object getAfterHoursChangeRealtime(){return (java.lang.Object) _fields.get("AfterHoursChangeRealtime");}
        public void setAfterHoursChangeRealtime(java.lang.Object AfterHoursChangeRealtime){_fields.put("AfterHoursChangeRealtime", AfterHoursChangeRealtime);}

        public java.lang.String getDividendShare(){return (java.lang.String) _fields.get("DividendShare");}
        public void setDividendShare(java.lang.String DividendShare){_fields.put("DividendShare", DividendShare);}

        public java.util.Date getLastTradeDate(){return (java.util.Date) _fields.get("LastTradeDate");}
        public void setLastTradeDate(java.util.Date LastTradeDate){_fields.put("LastTradeDate", LastTradeDate);}

        public java.lang.Object getTradeDate(){return (java.lang.Object) _fields.get("TradeDate");}
        public void setTradeDate(java.lang.Object TradeDate){_fields.put("TradeDate", TradeDate);}

        public java.lang.String getEarningsShare(){return (java.lang.String) _fields.get("EarningsShare");}
        public void setEarningsShare(java.lang.String EarningsShare){_fields.put("EarningsShare", EarningsShare);}

        public java.lang.Object getErrorIndicationreturnedforsymbolchangedinvalid(){return (java.lang.Object) _fields.get("ErrorIndicationreturnedforsymbolchangedinvalid");}
        public void setErrorIndicationreturnedforsymbolchangedinvalid(java.lang.Object ErrorIndicationreturnedforsymbolchangedinvalid){_fields.put("ErrorIndicationreturnedforsymbolchangedinvalid", ErrorIndicationreturnedforsymbolchangedinvalid);}

        public java.lang.String getEPSEstimateCurrentYear(){return (java.lang.String) _fields.get("EPSEstimateCurrentYear");}
        public void setEPSEstimateCurrentYear(java.lang.String EPSEstimateCurrentYear){_fields.put("EPSEstimateCurrentYear", EPSEstimateCurrentYear);}

        public java.lang.String getEPSEstimateNextYear(){return (java.lang.String) _fields.get("EPSEstimateNextYear");}
        public void setEPSEstimateNextYear(java.lang.String EPSEstimateNextYear){_fields.put("EPSEstimateNextYear", EPSEstimateNextYear);}

        public java.lang.String getEPSEstimateNextQuarter(){return (java.lang.String) _fields.get("EPSEstimateNextQuarter");}
        public void setEPSEstimateNextQuarter(java.lang.String EPSEstimateNextQuarter){_fields.put("EPSEstimateNextQuarter", EPSEstimateNextQuarter);}

        public java.lang.String getDaysLow(){return (java.lang.String) _fields.get("DaysLow");}
        public void setDaysLow(java.lang.String DaysLow){_fields.put("DaysLow", DaysLow);}

        public java.lang.String getDaysHigh(){return (java.lang.String) _fields.get("DaysHigh");}
        public void setDaysHigh(java.lang.String DaysHigh){_fields.put("DaysHigh", DaysHigh);}

        public java.lang.String getYearLow(){return (java.lang.String) _fields.get("YearLow");}
        public void setYearLow(java.lang.String YearLow){_fields.put("YearLow", YearLow);}

        public java.lang.String getYearHigh(){return (java.lang.String) _fields.get("YearHigh");}
        public void setYearHigh(java.lang.String YearHigh){_fields.put("YearHigh", YearHigh);}

        public java.lang.Object getHoldingsGainPercent(){return (java.lang.Object) _fields.get("HoldingsGainPercent");}
        public void setHoldingsGainPercent(java.lang.Object HoldingsGainPercent){_fields.put("HoldingsGainPercent", HoldingsGainPercent);}

        public java.lang.Object getAnnualizedGain(){return (java.lang.Object) _fields.get("AnnualizedGain");}
        public void setAnnualizedGain(java.lang.Object AnnualizedGain){_fields.put("AnnualizedGain", AnnualizedGain);}

        public java.lang.Object getHoldingsGain(){return (java.lang.Object) _fields.get("HoldingsGain");}
        public void setHoldingsGain(java.lang.Object HoldingsGain){_fields.put("HoldingsGain", HoldingsGain);}

        public java.lang.Object getHoldingsGainPercentRealtime(){return (java.lang.Object) _fields.get("HoldingsGainPercentRealtime");}
        public void setHoldingsGainPercentRealtime(java.lang.Object HoldingsGainPercentRealtime){_fields.put("HoldingsGainPercentRealtime", HoldingsGainPercentRealtime);}

        public java.lang.Object getHoldingsGainRealtime(){return (java.lang.Object) _fields.get("HoldingsGainRealtime");}
        public void setHoldingsGainRealtime(java.lang.Object HoldingsGainRealtime){_fields.put("HoldingsGainRealtime", HoldingsGainRealtime);}

        public java.lang.Object getMoreInfo(){return (java.lang.Object) _fields.get("MoreInfo");}
        public void setMoreInfo(java.lang.Object MoreInfo){_fields.put("MoreInfo", MoreInfo);}

        public java.lang.Object getOrderBookRealtime(){return (java.lang.Object) _fields.get("OrderBookRealtime");}
        public void setOrderBookRealtime(java.lang.Object OrderBookRealtime){_fields.put("OrderBookRealtime", OrderBookRealtime);}

        public java.lang.String getMarketCapitalization(){return (java.lang.String) _fields.get("MarketCapitalization");}
        public void setMarketCapitalization(java.lang.String MarketCapitalization){_fields.put("MarketCapitalization", MarketCapitalization);}

        public java.lang.Object getMarketCapRealtime(){return (java.lang.Object) _fields.get("MarketCapRealtime");}
        public void setMarketCapRealtime(java.lang.Object MarketCapRealtime){_fields.put("MarketCapRealtime", MarketCapRealtime);}

        public java.lang.String getEBITDA(){return (java.lang.String) _fields.get("EBITDA");}
        public void setEBITDA(java.lang.String EBITDA){_fields.put("EBITDA", EBITDA);}

        public java.lang.String getChangeFromYearLow(){return (java.lang.String) _fields.get("ChangeFromYearLow");}
        public void setChangeFromYearLow(java.lang.String ChangeFromYearLow){_fields.put("ChangeFromYearLow", ChangeFromYearLow);}

        public java.lang.String getPercentChangeFromYearLow(){return (java.lang.String) _fields.get("PercentChangeFromYearLow");}
        public void setPercentChangeFromYearLow(java.lang.String PercentChangeFromYearLow){_fields.put("PercentChangeFromYearLow", PercentChangeFromYearLow);}

        public java.lang.Object getLastTradeRealtimeWithTime(){return (java.lang.Object) _fields.get("LastTradeRealtimeWithTime");}
        public void setLastTradeRealtimeWithTime(java.lang.Object LastTradeRealtimeWithTime){_fields.put("LastTradeRealtimeWithTime", LastTradeRealtimeWithTime);}

        public java.lang.Object getChangePercentRealtime(){return (java.lang.Object) _fields.get("ChangePercentRealtime");}
        public void setChangePercentRealtime(java.lang.Object ChangePercentRealtime){_fields.put("ChangePercentRealtime", ChangePercentRealtime);}

        public java.lang.String getChangeFromYearHigh(){return (java.lang.String) _fields.get("ChangeFromYearHigh");}
        public void setChangeFromYearHigh(java.lang.String ChangeFromYearHigh){_fields.put("ChangeFromYearHigh", ChangeFromYearHigh);}

        public java.lang.String getPercebtChangeFromYearHigh(){return (java.lang.String) _fields.get("PercebtChangeFromYearHigh");}
        public void setPercebtChangeFromYearHigh(java.lang.String PercebtChangeFromYearHigh){_fields.put("PercebtChangeFromYearHigh", PercebtChangeFromYearHigh);}

        public java.lang.String getLastTradeWithTime(){return (java.lang.String) _fields.get("LastTradeWithTime");}
        public void setLastTradeWithTime(java.lang.String LastTradeWithTime){_fields.put("LastTradeWithTime", LastTradeWithTime);}

        public java.lang.String getLastTradePriceOnly(){return (java.lang.String) _fields.get("LastTradePriceOnly");}
        public void setLastTradePriceOnly(java.lang.String LastTradePriceOnly){_fields.put("LastTradePriceOnly", LastTradePriceOnly);}

        public java.lang.Object getHighLimit(){return (java.lang.Object) _fields.get("HighLimit");}
        public void setHighLimit(java.lang.Object HighLimit){_fields.put("HighLimit", HighLimit);}

        public java.lang.Object getLowLimit(){return (java.lang.Object) _fields.get("LowLimit");}
        public void setLowLimit(java.lang.Object LowLimit){_fields.put("LowLimit", LowLimit);}

        public java.lang.String getDaysRange(){return (java.lang.String) _fields.get("DaysRange");}
        public void setDaysRange(java.lang.String DaysRange){_fields.put("DaysRange", DaysRange);}

        public java.lang.Object getDaysRangeRealtime(){return (java.lang.Object) _fields.get("DaysRangeRealtime");}
        public void setDaysRangeRealtime(java.lang.Object DaysRangeRealtime){_fields.put("DaysRangeRealtime", DaysRangeRealtime);}

        public java.lang.String getFiftydayMovingAverage(){return (java.lang.String) _fields.get("FiftydayMovingAverage");}
        public void setFiftydayMovingAverage(java.lang.String FiftydayMovingAverage){_fields.put("FiftydayMovingAverage", FiftydayMovingAverage);}

        public java.lang.String getTwoHundreddayMovingAverage(){return (java.lang.String) _fields.get("TwoHundreddayMovingAverage");}
        public void setTwoHundreddayMovingAverage(java.lang.String TwoHundreddayMovingAverage){_fields.put("TwoHundreddayMovingAverage", TwoHundreddayMovingAverage);}

        public java.lang.String getChangeFromTwoHundreddayMovingAverage(){return (java.lang.String) _fields.get("ChangeFromTwoHundreddayMovingAverage");}
        public void setChangeFromTwoHundreddayMovingAverage(java.lang.String ChangeFromTwoHundreddayMovingAverage){_fields.put("ChangeFromTwoHundreddayMovingAverage", ChangeFromTwoHundreddayMovingAverage);}

        public java.lang.String getPercentChangeFromTwoHundreddayMovingAverage(){return (java.lang.String) _fields.get("PercentChangeFromTwoHundreddayMovingAverage");}
        public void setPercentChangeFromTwoHundreddayMovingAverage(java.lang.String PercentChangeFromTwoHundreddayMovingAverage){_fields.put("PercentChangeFromTwoHundreddayMovingAverage", PercentChangeFromTwoHundreddayMovingAverage);}

        public java.lang.String getChangeFromFiftydayMovingAverage(){return (java.lang.String) _fields.get("ChangeFromFiftydayMovingAverage");}
        public void setChangeFromFiftydayMovingAverage(java.lang.String ChangeFromFiftydayMovingAverage){_fields.put("ChangeFromFiftydayMovingAverage", ChangeFromFiftydayMovingAverage);}

        public java.lang.String getPercentChangeFromFiftydayMovingAverage(){return (java.lang.String) _fields.get("PercentChangeFromFiftydayMovingAverage");}
        public void setPercentChangeFromFiftydayMovingAverage(java.lang.String PercentChangeFromFiftydayMovingAverage){_fields.put("PercentChangeFromFiftydayMovingAverage", PercentChangeFromFiftydayMovingAverage);}

        public java.lang.String getName(){return (java.lang.String) _fields.get("Name");}
        public void setName(java.lang.String Name){_fields.put("Name", Name);}

        public java.lang.Object getNotes(){return (java.lang.Object) _fields.get("Notes");}
        public void setNotes(java.lang.Object Notes){_fields.put("Notes", Notes);}

        public java.lang.String getOpen(){return (java.lang.String) _fields.get("Open");}
        public void setOpen(java.lang.String Open){_fields.put("Open", Open);}

        public java.lang.String getPreviousClose(){return (java.lang.String) _fields.get("PreviousClose");}
        public void setPreviousClose(java.lang.String PreviousClose){_fields.put("PreviousClose", PreviousClose);}

        public java.lang.Object getPricePaid(){return (java.lang.Object) _fields.get("PricePaid");}
        public void setPricePaid(java.lang.Object PricePaid){_fields.put("PricePaid", PricePaid);}

        public java.util.Date getChangeinPercent(){return (java.util.Date) _fields.get("ChangeinPercent");}
        public void setChangeinPercent(java.util.Date ChangeinPercent){_fields.put("ChangeinPercent", ChangeinPercent);}

        public java.lang.String getPriceSales(){return (java.lang.String) _fields.get("PriceSales");}
        public void setPriceSales(java.lang.String PriceSales){_fields.put("PriceSales", PriceSales);}

        public java.lang.String getPriceBook(){return (java.lang.String) _fields.get("PriceBook");}
        public void setPriceBook(java.lang.String PriceBook){_fields.put("PriceBook", PriceBook);}

        public java.util.Date getExDividendDate(){return (java.util.Date) _fields.get("ExDividendDate");}
        public void setExDividendDate(java.util.Date ExDividendDate){_fields.put("ExDividendDate", ExDividendDate);}

        public java.lang.String getPERatio(){return (java.lang.String) _fields.get("PERatio");}
        public void setPERatio(java.lang.String PERatio){_fields.put("PERatio", PERatio);}

        public java.util.Date getDividendPayDate(){return (java.util.Date) _fields.get("DividendPayDate");}
        public void setDividendPayDate(java.util.Date DividendPayDate){_fields.put("DividendPayDate", DividendPayDate);}

        public java.lang.Object getPERatioRealtime(){return (java.lang.Object) _fields.get("PERatioRealtime");}
        public void setPERatioRealtime(java.lang.Object PERatioRealtime){_fields.put("PERatioRealtime", PERatioRealtime);}

        public java.lang.String getPEGRatio(){return (java.lang.String) _fields.get("PEGRatio");}
        public void setPEGRatio(java.lang.String PEGRatio){_fields.put("PEGRatio", PEGRatio);}

        public java.lang.String getPriceEPSEstimateCurrentYear(){return (java.lang.String) _fields.get("PriceEPSEstimateCurrentYear");}
        public void setPriceEPSEstimateCurrentYear(java.lang.String PriceEPSEstimateCurrentYear){_fields.put("PriceEPSEstimateCurrentYear", PriceEPSEstimateCurrentYear);}

        public java.lang.String getPriceEPSEstimateNextYear(){return (java.lang.String) _fields.get("PriceEPSEstimateNextYear");}
        public void setPriceEPSEstimateNextYear(java.lang.String PriceEPSEstimateNextYear){_fields.put("PriceEPSEstimateNextYear", PriceEPSEstimateNextYear);}

        public java.lang.String getSymbol(){return (java.lang.String) _fields.get("Symbol");}
        public void setSymbol(java.lang.String Symbol){_fields.put("Symbol", Symbol);}

        public java.lang.Object getSharesOwned(){return (java.lang.Object) _fields.get("SharesOwned");}
        public void setSharesOwned(java.lang.Object SharesOwned){_fields.put("SharesOwned", SharesOwned);}

        public java.lang.String getShortRatio(){return (java.lang.String) _fields.get("ShortRatio");}
        public void setShortRatio(java.lang.String ShortRatio){_fields.put("ShortRatio", ShortRatio);}

        public java.lang.String getLastTradeTime(){return (java.lang.String) _fields.get("LastTradeTime");}
        public void setLastTradeTime(java.lang.String LastTradeTime){_fields.put("LastTradeTime", LastTradeTime);}

        public java.lang.Object getTickerTrend(){return (java.lang.Object) _fields.get("TickerTrend");}
        public void setTickerTrend(java.lang.Object TickerTrend){_fields.put("TickerTrend", TickerTrend);}

        public java.lang.String getOneyrTargetPrice(){return (java.lang.String) _fields.get("OneyrTargetPrice");}
        public void setOneyrTargetPrice(java.lang.String OneyrTargetPrice){_fields.put("OneyrTargetPrice", OneyrTargetPrice);}

        public java.lang.String getVolume(){return (java.lang.String) _fields.get("Volume");}
        public void setVolume(java.lang.String Volume){_fields.put("Volume", Volume);}

        public java.lang.Object getHoldingsValue(){return (java.lang.Object) _fields.get("HoldingsValue");}
        public void setHoldingsValue(java.lang.Object HoldingsValue){_fields.put("HoldingsValue", HoldingsValue);}

        public java.lang.Object getHoldingsValueRealtime(){return (java.lang.Object) _fields.get("HoldingsValueRealtime");}
        public void setHoldingsValueRealtime(java.lang.Object HoldingsValueRealtime){_fields.put("HoldingsValueRealtime", HoldingsValueRealtime);}

        public java.lang.String getYearRange(){return (java.lang.String) _fields.get("YearRange");}
        public void setYearRange(java.lang.String YearRange){_fields.put("YearRange", YearRange);}

        public java.lang.Object getDaysValueChange(){return (java.lang.Object) _fields.get("DaysValueChange");}
        public void setDaysValueChange(java.lang.Object DaysValueChange){_fields.put("DaysValueChange", DaysValueChange);}

        public java.lang.Object getDaysValueChangeRealtime(){return (java.lang.Object) _fields.get("DaysValueChangeRealtime");}
        public void setDaysValueChangeRealtime(java.lang.Object DaysValueChangeRealtime){_fields.put("DaysValueChangeRealtime", DaysValueChangeRealtime);}

        public java.lang.String getStockExchange(){return (java.lang.String) _fields.get("StockExchange");}
        public void setStockExchange(java.lang.String StockExchange){_fields.put("StockExchange", StockExchange);}

        public java.lang.String getDividendYield(){return (java.lang.String) _fields.get("DividendYield");}
        public void setDividendYield(java.lang.String DividendYield){_fields.put("DividendYield", DividendYield);}

        public java.util.Date getPercentChange(){return (java.util.Date) _fields.get("PercentChange");}
        public void setPercentChange(java.util.Date PercentChange){_fields.put("PercentChange", PercentChange);}


      }

    }

  }

}
