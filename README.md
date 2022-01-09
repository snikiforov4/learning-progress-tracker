# Learning Progress Tracker

<h5>Description</h5>

<p>A program that keeps track of the learning progress of multiple students.</p>

<p>The program designed to interact with user through a command line interface. All commands are case-insensitive and responses kept reasonably informative.</p>

<h5>List of commands</h5>

<ol>
	<li><code>add students</code> - enters "adding new students" mode</li>
	<li><code>exit</code> - once a user enters it, the program quit.</li>
    <li><code>list</code> - prints list of student IDs.</li>
	<li><code>add points</code> - the program must update student learning progress.</li>
	<li><code>find</code> - the program print details of the student according to the specified ID.</li>
	<li><code>statistics</code> - program should print overall statistic and statistic by course</li>
	<li><code>notify</code> - "notifies" students about finishing course.</li>
</ol>

<h5>User registration</h5>

<p>To register user the following credentials must be provided: first and last name and a valid email address.</p>
<p>Users full names should include the first and the last name. Accept only ASCII characters, from <code>A</code> to <code>Z</code> and from <code>a</code> to <code>z</code> as well as hyphens <code>-</code> and apostrophes <code>'</code>. For example, <strong>Jean-Claude</strong> and <strong>O'Neill</strong> are valid names, but <strong>Stanisław</strong> <strong>Oğuz</strong> is not(English-alphabet letters only).</p>
<p>Some students may have really long names like Robert Jemison Van de Graaff or John Ronald Reuel Tolkien. In this case, used the following convention: the first part of the full name before the first blank space is the first name, and the rest of the full name treated as the last name.</p>
<p>Unique ID is assigned to each student so that you can access their records using the id. Email addresses used as users identity. This means that any email address can be used only once for registration.</p>

<h5>Updating user progress</h5>

<p>The learning platform offers four courses: Java, Data Structures and Algorithms (DSA), Databases, and Spring. Each student can take any (or all) of these courses, complete tasks, pass tests, and submit their homework to receive points. By completing any task of any course, a student earns credit points that will be added to the student's total course score. These points can be zero, but they can't be negative.</p>

<p>Use the following input format to update records: <code>studentId number number number number</code>. The first element is a student's ID, and the rest four elements are points earned by the student in the courses. For example:</p>
<pre>25841 4 10 5 0
28405 0 8 7 5</pre>
