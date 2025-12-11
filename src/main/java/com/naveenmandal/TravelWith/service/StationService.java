package com.naveenmandal.TravelWith.service;

import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class StationService {

    public List<String> getAllStations() {
        List<String> stations = Arrays.asList(
                // --- MADHYA PRADESH (MP) ---
                "Bhopal Junction", "Rani Kamlapati", "Indore Junction", "Gwalior Junction",
                "Jabalpur", "Ujjain Junction", "Ratlam Junction", "Itarsi Junction",
                "Katni Junction", "Satna", "Rewa", "Saugor", "Bina Junction",
                "Hoshangabad", "Vidisha", "Dewas", "Khandwa", "Pipariya",

                // --- UTTAR PRADESH (UP) ---
                "Lucknow Charbagh", "Lucknow Junction", "Kanpur Central", "Varanasi Junction",
                "Prayagraj Junction (Allahabad)", "Agra Cantt", "Agra Fort", "Mathura Junction",
                "Gorakhpur Junction", "Jhansi Junction", "Meerut City", "Ghaziabad",
                "Aligarh Junction", "Moradabad", "Bareilly", "Faizabad", "Ayodhya Cantt",
                "Mughalsarai (Pt. Deen Dayal Upadhyaya)",

                // --- SOUTH INDIA (TN, KL, KA, AP, TS) ---
                "Chennai Central (MGR)", "Chennai Egmore", "Coimbatore Junction", "Madurai Junction",
                "Tiruchchirappalli (Trichy)", "Salem Junction", "Erode Junction", "Tirunelveli",
                "Kanyakumari", "Vellore Katpadi",

                "Bengaluru City (KSR)", "Bengaluru Cantt", "Yesvantpur Junction", "SMVT Bengaluru",
                "Mysuru Junction", "Hubballi Junction", "Mangaluru Central", "Belagavi",

                "Thiruvananthapuram Central", "Ernakulam Junction (South)", "Ernakulam Town (North)",
                "Kozhikode (Calicut)", "Thrissur", "Kollam Junction", "Alappuzha",

                "Hyderabad Deccan (Nampally)", "Secunderabad Junction", "Kacheguda",
                "Visakhapatnam Junction", "Vijayawada Junction", "Tirupati", "Guntur",
                "Nellore", "Rajahmundry", "Warangal",

                // --- MAHARASHTRA ---
                "Mumbai CSMT", "Mumbai Central", "Dadar", "Lokmanya Tilak Terminus (LTT)",
                "Pune Junction", "Nagpur Junction", "Nashik Road", "Aurangabad", "Thane",
                "Kalyan Junction",

                // --- DELHI / NCR ---
                "New Delhi", "Old Delhi", "Hazrat Nizamuddin", "Anand Vihar Terminal",
                "Delhi Sarai Rohilla",

                // --- RAJASTHAN ---
                "Jaipur Junction", "Jodhpur Junction", "Udaipur City", "Ajmer Junction",
                "Kota Junction", "Bikaner",

                // --- OTHERS (East/West) ---
                "Howrah Junction", "Sealdah", "Kolkata", "Patna Junction", "Gaya",
                "Ahmedabad Junction", "Surat", "Vadodara", "Bhubaneswar", "Puri",
                "Guwahati", "Raipur Junction", "Bilaspur"
        );

        // Sort
        Collections.sort(stations);
        return stations;
    }
}