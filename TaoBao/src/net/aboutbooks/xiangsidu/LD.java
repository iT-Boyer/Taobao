package net.aboutbooks.xiangsidu;
/**  
    Levenshtein distance�������ɶ����ѧ��Vladimir Levenshtein��1965�귢������������������������ƴ�������Խ���edit distance���༭���룩��  
    Levenshtein distance����������  
      
    Spell checking(ƴд���)   
    Speech recognition(���ʶ��)   
    DNA analysis(DNA����)   
    Plagiarism detection(��Ϯ���)   
    LD��m*n�ľ���洢����ֵ���㷨��Ź��̣�  
    str1��str2�ĳ���Ϊ0������һ���ַ����ĳ��ȡ�   
    ��ʼ��(n+1)*(m+1)�ľ���d�����õ�һ�к��е�ֵ��0��ʼ������   
    ɨ�����ַ�����n*m���ģ��������str1[i] == str2[j]����temp��¼����Ϊ0������temp��Ϊ1��Ȼ���ھ���d[i][j]����d[i-1][j]+1 ��d[i][j-1]+1��d[i-1][j-1]+temp���ߵ���Сֵ��   
    ɨ����󣬷��ؾ�������һ��ֵ��d[n][m]  
 */  
public class LD   
{   
    /**  
     * ����ʸ������  
     * Levenshtein Distance(LD)   
     * @param str1 str1  
     * @param str2 str2  
     * @return ld  
     */  
    private int ld(String str1, String str2)   
    {   
        //Distance   
        int [][] d;    
        int n = str1.length();   
        int m = str2.length();   
        int i; //iterate str1   
        int j; //iterate str2   
        char ch1; //str1    
        char ch2; //str2     
        int temp;       
        if (n == 0)   
        {   
            return m;   
        }   
        if (m == 0)   
        {   
            return n;   
        }   
        d = new int[n + 1][m + 1];   
        for (i = 0; i <= n; i++)   
        {   d[i][0] = i;   
        }   
        for (j = 0; j <= m; j++)   
        {    
            d[0][j] = j;   
        }   
        for (i = 1; i <= n; i++)   
        {      
            ch1 = str1.charAt(i - 1);   
            //match str2      
            for (j = 1; j <= m; j++)   
            {   
                ch2 = str2.charAt(j - 1);   
                if (ch1 == ch2)   
                {   
                    temp = 0;   
                }   
                else  
                {   
                    temp = 1;   
                }   
    
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);   
            }   
        }   
        return d[n][m];   
    }   
    
    private int min(int one, int two, int three)   
    {   
        int min = one;   
        if (two < min)   
        {   
            min = two;   
        }   
        if (three < min)   
        {   
            min = three;   
        }   
        return min;   
    }   
    
    /**  
     * �������ƶ�  
     * @param str1 str1  
     * @param str2 str2  
     * @return sim    
     */  
    public double compare(String str1, String str2)   
    {   
    	str1=str1.replaceAll("[(<)|(>)|(\\[)|(\\])|({)]|(})|(��)|(��)|(\\.)|(��)|(��)|(:)|(��)|(��)|(��)|(\\s*)|(,)|(��)", "");
    	str2=str2.replaceAll("[(<)|(>)|(\\[)|(\\])|({)]|(})|(��)|(��)|(\\.)|(��)|(��)|(:)|(��)|(��)|(��)|(\\s*)|(,)|(��)", "");
        int ld = ld(str1.trim(), str2.trim());   
        return 1 - (double) ld / Math.max(str1.length(), str2.length());   
    }   
    /**
     * �������ַ���
     * */
    public double compareNoParser(String str1, String str2)   
    {   
        int ld = ld(str1.trim(), str2.trim());   
        return 1 - (double) ld / Math.max(str1.length(), str2.length());   
    }   
    /**  
     * ����  
     * @param args  
     */  
    public static void main(String[] args)   
    {   
        LD ld = new LD();   
        double num = ld.compareNoParser("�����Ӷ�����ʼ", "����ʼ�ڶ���");   
        System.out.println(num);   
    }   
    
}  
