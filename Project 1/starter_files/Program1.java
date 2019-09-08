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
        ArrayList<ArrayList<Integer>> studentScores = new ArrayList<>(internshipCount);
        for(int i = 0;i<internshipCount;i++){
            internshipPref.add(i, new ArrayList<>(studentCount));
            studentScores.add(i, new ArrayList<>(studentCount));
        }
        for(int i = 0;i<internshipCount;i++){
            for(int j = 0;j<studentCount;j++){
                Integer studentScore = computeInternshipStudentScore(student_GPA.get(j),student_months.get(j),student_projects.get(j),internship_weights.get(i).get(0) ,internship_weights.get(i).get(1),internship_weights.get(i).get(2)).intValue();
                studentScores.get(i).add(j,studentScore);
                internshipPref.get(i).add(j,j);
            }
        }
        for(int s = 0;s<internshipPref.size();s++){
            //insertion sort for each row
            for(int i =0;i<studentCount;i++){
                int element = studentScores.get(s).get(i);
                int index = internshipPref.get(s).get(i);
                for(int j = i-1;j>=0;j--){
                    if(studentScores.get(s).get(j)<element){
                        internshipPref.get(s).set(j+1,internshipPref.get(s).get(j));
                        internshipPref.get(s).set(j,index);
                        studentScores.get(s).set(j+1,studentScores.get(s).get(j));
                        studentScores.get(s).set(j,element);
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
        //I have both preference lists
        //traverse the entire matching
        //check for both types of instabilities
        for(int i = 0;i<marriage.getStudentCount();i++){
            if(student_matching.get(i) != -1){ //student has an internship check for instability A
                int S_Internship = student_matching.get(i); //the internship the student has -> student S
                //find another student that I prefers to student S ie find S'
                ArrayList<Integer> InternshipI_Preferences = marriage.getInternshipPreference().get(S_Internship);
                for(int j = 0;j<marriage.getStudentCount();j++){
                    int Iprime = student_matching.get(j);
                    if(InternshipI_Preferences.get(j) == i)
                        break;
                    else{ //another student has higher preference ie I prefers S' to s
                        //check if s prefers I to I'
                        for(int k = 0;k<marriage.getInternshipCount();k++){
                            //go through s' preference list and see which comes first
                            if(marriage.getStudentPreference().get(j).get(k) == Iprime || S_Internship == Iprime)
                                break;
                            else if(marriage.getStudentPreference().get(j).get(k) == S_Internship)
                                return false; //I is higher on s' preference list
                        }
                    }
                }
            }
            else{ //student does not have an internship check for instability B
                for(int j = 0;j<marriage.getInternshipCount();j++) {
                    //go through all internships and see if any internship prefers Student s (i) to their match
                    //start with internship 0 ... n (j)
                    //find the student that has that internship
                    //see in the internships (j) preference list which comes first i or the student already matched
                    int studentPrime = 0;
                    for(int k = 0;k<marriage.getStudentCount();k++){
                        if(student_matching.get(k) == j)
                            studentPrime = k;
                    }
                    for(int k =0;k<marriage.getStudentCount();k++){
                        if(marriage.getInternshipPreference().get(j).get(k) == studentPrime)
                            break;
                        else if(marriage.getInternshipPreference().get(j).get(k) == i)
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
