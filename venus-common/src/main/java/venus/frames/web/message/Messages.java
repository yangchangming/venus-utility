package venus.frames.web.message;

import java.io.Serializable;
import java.util.*;

/**
 * @author wujun
 */
public class Messages implements Serializable, List{

	private List m_aryLst = new ArrayList();
	
	public Messages(){
		
	
	}
	
	public void pushMessage(MessageWrapper msg){
		
		m_aryLst.add(msg);
	
	}

	/* （非 Javadoc）
	 * @see java.util.List#size()
	 */
	public int size() {

		return m_aryLst.size();
	}

	/* （非 Javadoc）
	 * @see java.util.List#clear()
	 */
	public void clear() {
		
		m_aryLst.clear();
		
	}

	/* （非 Javadoc）
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty() {

		return m_aryLst.isEmpty();
	}

	/* （非 Javadoc）
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray() {

		return m_aryLst.toArray();
	}

	/* （非 Javadoc）
	 * @see java.util.List#get(int)
	 */
	public Object get(int arg0) {

		return m_aryLst.get(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.List#remove(int)
	 */
	public Object remove(int arg0) {

		return m_aryLst.remove(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int arg0, Object arg1) {

		m_aryLst.add(arg0, arg1);
	}

	/* （非 Javadoc）
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object arg0) {

		return m_aryLst.indexOf(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object arg0) {

		return m_aryLst.lastIndexOf(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(Object arg0) {

		return m_aryLst.add(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains(Object arg0) {

		return m_aryLst.contains(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object arg0) {

		return m_aryLst.remove(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int arg0, Collection arg1) {

		return m_aryLst.addAll(arg0, arg1);
	}

	/* （非 Javadoc）
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection arg0) {

		return m_aryLst.addAll(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection arg0) {

		return m_aryLst.containsAll(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection arg0) {

		return m_aryLst.removeAll(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection arg0) {

		return m_aryLst.retainAll(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.List#iterator()
	 */
	public Iterator iterator() {

		return m_aryLst.iterator();
	}

	/* （非 Javadoc）
	 * @see java.util.List#subList(int, int)
	 */
	public List subList(int arg0, int arg1) {

		return m_aryLst.subList(arg0, arg1);
	}

	/* （非 Javadoc）
	 * @see java.util.List#listIterator()
	 */
	public ListIterator listIterator() {

		return m_aryLst.listIterator();
	}

	/* （非 Javadoc）
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator listIterator(int arg0) {

		return m_aryLst.listIterator(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public Object set(int arg0, Object arg1) {

		return m_aryLst.set(arg0, arg1);
	}

	/* （非 Javadoc）
	 * @see java.util.List#toArray(java.lang.Object[])
	 */
	public Object[] toArray(Object[] arg0) {

		return m_aryLst.toArray(arg0);
	}
	
}
