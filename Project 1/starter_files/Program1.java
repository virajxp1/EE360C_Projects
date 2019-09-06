/*
 * Name: <Viraj Parikh>
 * EID: <VHP286>
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    private int getPreferenceIndex(ArrayList<Integer> preferenceList, int entity) {
        int index = -1;
        for (int i = 0; i < preferenceList.size(); i++) {
            if (preferenceList.get(i) == entity) {
                index = i;
                break;
            }
        }
        return index;
    }
    /**
     * Compute the preference lists for each internship, given weights and student metrics.
     * Return a ArrayList<ArrayList<Integer>> prefs, where prefs.get(i) is the ordered list of preferred students for
     * internship i, with length studentCount.
     */
    public static ArrayList<ArrayList<Integer>> computeInternshipPreferences(int internshipCount, int studentCount,
                                                                      ArrayList<ArrayList<Integer>>internship_weights,
                                                                      ArrayList<Double> student_GPA,
                                                                      ArrayList<Integer> student_months,
                                                                      ArrayList<Integer> student_projects){
        ArrayList<ArrayList<Integer>> internshipPref = new ArrayList<>(internshipCount);
        for(int i = 0;i<studentCount;i++){
            internshipPref.add(i, new ArrayList<>(studentCount));
        }
        for(int i = 0;i<internshipCount;i++){
            for(int j = 0;j<studentCount;j++){
                Integer studentScore = computeInternshipStudentScore(student_GPA.get(j),student_months.get(j),student_projects.get(j),internship_weights.get(i).get(0) ,internship_weights.get(i).get(1),internship_weights.get(i).get(2)).intValue();
                internshipPref.get(i).add(j,studentScore);
            }
        }
        for(int s = 0;s<internshipPref.size();s++){
            //insertion sort for each row
            for(int i =0;i<studentCount;i++){
                int element = internshipPref.get(s).get(i);
                for(int j = i-1;j>=0;j--){
                    if(internshipPref.get(s).get(j)<element){
                        internshipPref.get(s).set(j+1,internshipPref.get(s).get(j));
                        internshipPref.get(s).set(j,element);
                    }
                }
            }
        }
        //O(n^3)
        return internshipPref;
    }
    private static Double computeInternshipStudentScore(double studentGPA, int studentExp, int studentProjects, int
                                                        weightGPA, int weightExp, int weightProjects){
        return studentGPA*weightGPA+studentExp*weightExp+studentProjects*weightProjects;
    }

    /**
     * Determines whether a candidate Matching represents a solution to the Stable Marriage problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    public boolean isStableMatching(Matching marriage) {
        /* TODO implement this function
            Instability A:
            Student s has Internship I
            Student s' has Internship I'
            Internship I prefers s' to s
            Student s' prefers Internship I to I'
            -------------------------------------------------
            Instability B:
            Student s has internship I
            Student s' has no internship
            I prefers s' to s
            -------------------------------------------------
         */
        ArrayList<Integer> student_matching = marriage.getStudentMatching();
        for(int i = 0;i<student_matching.size();i++){
            int internship_matching = student_matching.get(i);
            if(internship_matching != -1){
                //Instability A
                ArrayList<Integer> I_pref = marriage.getInternshipPreference().get(internship_matching); //List of the preferences of Internship I
                Integer studentScore = computeInternshipStudentScore(marriage.getStudentGPA().get(i),marriage.getStudentMonths().get(i),marriage.getStudentProjects().get(i),marriage.getInternshipWeights().get(internship_matching).get(0),marriage.getInternshipWeights().get(internship_matching).get(1),marriage.getInternshipWeights().get(internship_matching).get(2)).intValue();
                //Find for all that students (Sn) that I prefers over S check if Sn prefers I to In
                int j = 0;
                while(I_pref.get(j)>=studentScore){
                    //I prefers the student with this score over S
                    //Find the student
                    int score = I_pref.get(j);
                    int studentN =0; //A student who is more prefered by the internship than S
                    for(int k = 0;k<studentScore;k++){ //find the index of the student
                        Integer tempScore = computeInternshipStudentScore(marriage.getStudentGPA().get(k),marriage.getStudentMonths().get(k),marriage.getStudentProjects().get(k),marriage.getInternshipWeights().get(internship_matching).get(0),marriage.getInternshipWeights().get(internship_matching).get(1),marriage.getInternshipWeights().get(internship_matching).get(2)).intValue();
                        if(score == tempScore){
                            studentN = k;
                            break;
                        }
                    }
                    int Internship_studentN = student_matching.get(studentN); //The internship S' is matched with
                    //Compare if s' prefers I over I'
                    ArrayList<Integer> Sn_prefList = marriage.getStudentPreference().get(studentN); //Sn preference list
                    //I = internship_matching
                    //I' = internship_studentN
                    for(int k = 0;k<internship_matching;k++){
                       if(Sn_prefList.get(k) == Internship_studentN) //I' higher on preference list so no instability
                            break;
                       if(Sn_prefList.get(k) == internship_matching) //Instability
                            return false;
                    }
                    j++;
                }
            }
            else{
                //Instability B
                //Find a person with no internship - s'
                //calculate their score
                //go through all internships and see if any internships prefer the student s' over current matched student s
                //can be done by seeing if the student s' has a higher score than the student s per every internship
                for(int j = 0;j<marriage.getStudentCount();j++){
                    if(j!=i){
                        //save the internship of the student at location j
                        //calculate the score of the person s based on the internship
                        //calculate the score of the person s' based on the internship
                        //if s' > s than s' is higher on the preference list of I
                        //since s' doesn't have an internship but is higher preferred than we have an instability
                        int s_internship = marriage.getStudentMatching().get(j);
                        Integer studentScoreS = computeInternshipStudentScore(marriage.getStudentGPA().get(j),marriage.getStudentMonths().get(j),marriage.getStudentProjects().get(j),marriage.getInternshipWeights().get(s_internship).get(0),marriage.getInternshipWeights().get(s_internship).get(1),marriage.getInternshipWeights().get(s_internship).get(2)).intValue();
                        Integer studentScoreSPrime = computeInternshipStudentScore(marriage.getStudentGPA().get(i),marriage.getStudentMonths().get(i),marriage.getStudentProjects().get(i),marriage.getInternshipWeights().get(s_internship).get(0),marriage.getInternshipWeights().get(s_internship).get(1),marriage.getInternshipWeights().get(s_internship).get(2)).intValue();
                        if(studentScoreSPrime>studentScoreS)
                            return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines a solution to the Stable Marriage problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageGaleShapley_studentoptimal(Matching marriage) {
        /* TODO implement this function */
        return null; /* TODO remove this line */
    }

    private ArrayList<Matching> getAllStableMarriages(Matching marriage) {
        ArrayList<Matching> marriages = new ArrayList<>();
        int n = marriage.getStudentCount();
        int slots = marriage.totalInternshipSlots();

        Permutation p = new Permutation(n, slots);
        Matching matching;
        while ((matching = p.getNextMatching(marriage)) != null) {
            if (isStableMatching(matching)) {
                marriages.add(matching);
            }
        }

        return marriages;
    }

    @Override
    public Matching stableMarriageBruteForce_studentoptimal(Matching marriage) {
        ArrayList<Matching> allStableMarriages = getAllStableMarriages(marriage);
        Matching studentOptimal = null;
        int n = marriage.getStudentCount();
        int m = marriage.getInternshipCount();
        System.out.println("student" + n + "internship" + m);
        ArrayList<ArrayList<Integer>> student_preference = marriage.getStudentPreference();

        //Construct an inverse list for constant access time
        ArrayList<ArrayList<Integer>> inverse_student_preference = new ArrayList<ArrayList<Integer>>(0) ;
        for (Integer i=0; i<n; i++) {
            ArrayList<Integer> inverse_preference_list = new ArrayList<Integer>(m) ;
            for (Integer j=0; j<m; j++)
                inverse_preference_list.add(-1) ;
            ArrayList<Integer> preference_list = student_preference.get(i) ;

            for (int j=0; j<m; j++) {
                inverse_preference_list.set(preference_list.get(j), j) ;
            }
            inverse_student_preference.add(inverse_preference_list) ;
        }

        // bestStudentMatching stores the rank of the best Internship each student matched to
        // over all stable matchings
        int[] bestStudentMatching = new int[marriage.getStudentCount()];
        Arrays.fill(bestStudentMatching, -1);
        for (Matching mar : allStableMarriages) {
            ArrayList<Integer> student_matching = mar.getStudentMatching();
            for (int i = 0; i < student_matching.size(); i++) {
                if (student_matching.get(i) != -1 && (bestStudentMatching[i] == -1 ||
                        inverse_student_preference.get(i).get(student_matching.get(i)) < bestStudentMatching[i])) {
                    bestStudentMatching[i] = inverse_student_preference.get(i).get(student_matching.get(i));
                    studentOptimal = mar;
                }
            }
        }

        return studentOptimal;
    }
}
